-module(dispatcher).
-behaviour(gen_server).

-export([dispatcher_initialization/0,forward_message/2, random_message/2]).
-export([init/1, handle_info/2, handle_call/3, handle_cast/2]).

-define(NUM_CLUSTER, 1).
-define(INIT_TIMEOUT, 5000).

%---------------------Structure of messages to send to dispatcher -----------------------------------
%   Base Message = {Command,Id,Data}
%   List of possible messages:
%     {create_auction,IdAuction,AuctionData};
%     {delete_auction,IdAuction,_};
%     {select_auction,IdAuction, IdUser};
%     {auction_list,IdAuction,_};
%     {auction_agent_list,IdAuction,_};
%     {make_bid,IdAuction,BidData};
%     {delete_bid,IdAuction,IdBid};
%
%     AuctionData = {id_auction, id_agent, name, image, description, end_date, min_price, min_raise, sale_quantity} auction data
%     BidData = {id_bid, id_user, timestamp,bid_value, quantity} a bid
%---------------------------------------------------------------------------------------

%Initialization phase: when a dispatcher starts, it requests the current state from a currently active dispatcher (if any) 
%and once received it sets it as the initial state
dispatcher_initialization() ->
  List = utility:pids_from_global_registry("d_"),
  if 
    List == [] -> [];
    true -> Pid = lists:nth(rand:uniform(length(List)),List),
            {TypeMsg, Msg} = utility:call(Pid,dispatcher_state),
            if 
              TypeMsg =/= ok -> dispatcher_initialization();
              true -> Msg
            end
  end.

%Dispatcher forwards an "auction request" to the cluster that deals with it.
forward_message({Command,Id,Data}, State) ->
  {_,Leader} = lists:keyfind((Id rem ?NUM_CLUSTER)+1, 1, State),
  Result = utility:call(Leader, {Command,Id,Data}),
  Result.

%Dispatcher forwards a "auction list" request to a cluster randomly. 
%This choice is due to the fact that "auction list" requests are read-only operations
%and Mnesia allows the data to be accessible from all nodes
random_message({Command,Id,Data}, State) ->
  {_,Leader} = lists:keyfind(rand:uniform(length(State)), 1, State),
  io:format("Leader: ~p\n", [Leader]),
  Result = utility:call(Leader, {Command,Id,Data}),
  io:format("Result: ~p\n", [Result]),

  Result.

%------ CALLBACK DISPATCHER ------------------------------------------------------------------------------------------

init([]) ->
  State = dispatcher_initialization(),
  {ok, State}.

handle_call({Command,Id,Data}, _From, State) ->
  case Command of
    auction_list -> Result = random_message({Command,Id,Data}, State);
    auction_agent_list -> Result = random_message({Command,Id,Data}, State);
    _ -> Result = forward_message({Command,Id,Data}, State)
  end,
  {reply, Result, State};

handle_call(dispatcher_state, _From, State) ->
  {reply, {ok,State}, State}.

handle_cast({leader_message,Cluster, Leader}, State) ->
  NewState = utility:modify_key_value_list(State, {Cluster, Leader}),
  io:format("~p Nuovo Stato: ~p\n", [self(), NewState]),
  {noreply, NewState}.


handle_info(Command, {Data,State}) ->
    io:format("received command ~p\n", [Command]),
    {noreply, {Data,State}}.


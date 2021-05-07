-module(dispatcher).
-behaviour(gen_server).

-export([dispatcher_initialization/0,forward_message/2, broadcast_message/2]).
-export([init/1, handle_call/3, handle_cast/2]).

-define(NUM_CLUSTER, 1).
-define(INIT_TIMEOUT, 5000).

%---------------------Structure of messages to send to dispatcher -----------------------------------
%   Base Message = {Command,Id,Data}
%   List of possible messages:
%     {create_auction,IdAuction,AuctionData};
%     {delete_auction,IdAuction,_};
%     {select_auction,IdAuction,_};
%     {auction_list,IdAuction,_};
%     {auction_agent_list,IdAuction,_};
%     {make_bid,IdAuction,BidData};
%     {delete_bid,IdAuction,IdBid};
%
%     AuctionData = {id_auction, id_agent, name, image, end_date, min_price, min_raise, sale_quantity} auction data
%     BidData = {id_bid, id_user, timestamp,bid_value, quantity} a bid
%---------------------------------------------------------------------------------------


%--------------------------------------------------------------------------

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

forward_message({Command,Id,Data}, State) ->
  {_,Leader} = lists:keyfind((Id rem ?NUM_CLUSTER)+1, 1, State),
  Result = utility:call(Leader, {Command,Id,Data}),
  Result.

broadcast_message(_, []) -> [];
broadcast_message(Message, [HeadState|T]) ->
  Leader = element(2,HeadState),
  {TypeMsg, Result} = utility:call(Leader, Message),
  if 
    TypeMsg ==  ok -> Result ++ broadcast_message(Message, T);
    true -> [] ++ broadcast_message(Message, T)
  end.

%---------------------------------------------------------------------------
init([]) ->
  State = dispatcher_initialization(),
  {ok, State}.

handle_call({Command,Id,Data}, _From, State) ->
  case Command of
    auction_list -> List = broadcast_message({Command,Id,Data}, State), Result = {ok,List};
    auctions_agent_list -> List = broadcast_message({Command,Id,Data}, State), Result = {ok,List};
    _ -> Result = forward_message({Command,Id,Data}, State)
  end,
  {reply, Result, State};

handle_call(dispatcher_state, _From, State) ->
  {reply, {ok,State}, State}.

handle_cast({leader_message,Cluster, Leader}, State) ->
  NewState = utility:modify_key_value_list(State, {Cluster, Leader}),
  io:format("~p Nuovo Stato: ~p\n", [self(), NewState]),
  {noreply, NewState}.


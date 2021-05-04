-module(dispatcher).
-behaviour(gen_server).

-export([forward_message/2, broadcast_message/2]).
-export([init/1, handle_call/3, handle_cast/2]).

-define(NUM_CLUSTER, 3).


forward_message({Command,Id,Data}, State) ->
  Leader = utility:extract_value_from_key(State, Id rem ?NUM_CLUSTER),
  Result = utility:handle_call({Command,Id,Data}, Leader),
  Result.

broadcast_message(_, []) -> [];
broadcast_message(Message, [HeadState|T]) ->
  Leader = element(2,HeadState),
  {TypeMsg, Result} = utility:handle_call(Message, Leader),
  if 
    TypeMsg ==  ok -> Result ++ broadcast_message(Message, T);
    true -> [] ++ broadcast_message(Message, T)
  end.

%---------------------------------------------------------------------------
init([]) ->
  {ok, []}.

handle_call({Command,Id,Data}, _From, State) ->
  case Command of
    auction_list -> List = broadcast_message({Command,Id,Data}, State), Result = {ok,List};
    auctions_agent_list -> List = broadcast_message({Command,Id,Data}, State), Result = {ok,List};
    true -> Result = forward_message({Command,Id,Data}, State)
  end,
  {reply, Result}.



handle_cast({leader_message,Cluster, Leader}, State) ->
  NewState = utility:modify_key_value_list(State, {Cluster, Leader}),
  {noreply, NewState}.






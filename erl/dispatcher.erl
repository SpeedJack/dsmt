-module(dispatcher).
-behaviour(gen_server).

-export([start_dispatchers/2, stop_dispatchers/2]).

-export([init/1, handle_call/3, handle_cast/2]).



%----- INITIALIZATION DISPATCHER --------------------------------------------------------------------
start_dispatchers(_, 0) -> ok;
start_dispatchers(Start, Num) ->
  Name = "d_" ++ integer_to_list(Start+Num-1) ++ "_" ++   atom_to_list(node()),
  gen_server:start({global, Name}, ?MODULE, [], []),
  start_dispatchers(Start, Num - 1).

stop_dispatchers(_ , 0) -> ok;
stop_dispatchers(Start, Num)->
    Name = "d_" ++ integer_to_list(Start+Num-1) ++ "_" ++   atom_to_list(node()),
    gen_server:stop({global, Name}),
    stop_dispatchers(Start, Num - 1).
%-------------------------------------------------------------------------------------------------------
%------ CALLBACK DISPATCHER ----------------------------------------------------------------------------

init([]) ->
  {ok, []}.


handle_call({Command,Id,Data}, _From, State) ->
  Leader = utility:extract_value_from_key(State, Id rem 3),
  Result = utility:handle_call({Command,Id,Data}, Leader),
  if
    Result == noproc -> {reply,error};
    Result == ok -> {reply,ok}
  end.


handle_cast({leader_message,Cluster, Leader}, State) ->
  NewState = utility:modify_key_value_list(State, {Cluster, Leader}),
  {reply, ok, NewState}.






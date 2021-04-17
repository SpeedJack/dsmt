-module(dispatcher).
-behavior(gen_server).

-export([start_dispatchers/1, stop_dispatchers/1]).
-export([init/1, handle_call/3]) .

start_dispatchers(0) -> ok;
start_dispatchers(Num) ->
  gen_server:start({global, "dispatcher_" ++ lists:flatten(io_lib:format("~p", [Num])) }, ?MODULE, [], []),
  start_dispatchers(Num - 1).

stop_dispatchers(0) -> ok;
stop_dispatchers(Num)->
    gen_server:stop({global, "dispatcher_" ++ lists:flatten(io_lib:format("~p", [Num])) }),
    stop_dispatchers(Num - 1).

init(_Args) ->
  {ok,[]}.   % general format: {ok, InitialState}

handle_call({create,Id}, _From, State) ->
    io:format("forward request\n"),
    NewState = State ++ [{Id,4}],
    {reply,{ok, {Id, 4}}, NewState}.

-module(dispatcher).
-behavior(gen_server).

-export([start_dispatchers/1, stop_dispatchers/1, update_dispatchers/1]).
-export([init/1, handle_call/3, handle_cast/2]) .

start_dispatchers(0) -> ok;
start_dispatchers(Num) ->
  Name = "dispatcher_" ++ lists:flatten(io_lib:format("~p", [Num])),
  gen_server:start({global, Name}, ?MODULE, [], []),
  start_dispatchers(Num - 1).

stop_dispatchers(0) -> ok;
stop_dispatchers(Num)->
    Name = "dispatcher_" ++ lists:flatten(io_lib:format("~p", [Num])),
    gen_server:stop({global, Name}),
    stop_dispatchers(Num - 1).



%-------------------------------------------------------------------------------------------------------

update_dispatchers(NewState) ->
  Names = utility:obtain_names_from_registry(global, dispatcher),
  lists:foreach( fun (Name) -> gen_server:cast({global,Name}, {update, NewState}) end , Names).
 
init([]) ->
  {ok, []}.   % general format: {ok, InitialState}

handle_call({create,Id, _}, _From, State) ->
    NewState = State ++ [{Id,4}],
    update_dispatchers(NewState),
    {reply,{ok, NewState}, NewState};

handle_call({_,_,_}, _From, State) ->
    {reply,{ok}, State}.

handle_cast({update,NewState}, _) ->
    io:format("Received New State: ~tp\n", NewState),
    {noreply, NewState}.
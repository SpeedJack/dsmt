-module(executor).
-behaviour(gen_server).

-export([init/1, handle_info/2, handle_cast/2, handle_call/3]).

%------ CALLBACK EXECUTOR ------------------------------------------------------------------------------------------

init(Cluster) ->
    io:format("Process executor ~p create\n", [self()]),
    State = bully:initialization(Cluster),
    {ok, {[], State}}.

%------ CALLBACKS: BULLY ALGORITHM ------------------------------------------------------------------------------

handle_info(Command, {Data,State}) ->
    case Command of
        leader_timeout -> NewState = bully:handle_leader_timeout(State);
        ok_timeout -> NewState = bully:handle_ok_timeout(State)
    end,
    {noreply, {Data,NewState}}.

handle_cast(Command, {Data, State}) ->
    case Command of
        ok_message -> NewState = bully:handle_ok_message(State);
        {hello_message, From} -> NewState =  bully:handle_hello_message(From, State);
        {election_message, From} -> NewState = bully:handle_election_message(From, State);
        {leader_message, _, Leader} -> NewState = bully:handle_leader_message(Leader, State)
    end,
    {noreply, {Data,NewState}}.

%-------- CALLBACKS: AUCTIONS CORE ---------------------------------------------------------------------------------------------


handle_call({Command,Id,Payload},_From, {Data, State}) ->
    case Command of
        create_auction -> NewData = auctions_core:create_auction({Command,Id,Payload}, {Data,State}), {reply, ok, {NewData, State}};
        delete_auction -> NewData = auctions_core:delete_auction({Command,Id,Payload}, {Data,State}), {reply, ok, {NewData, State}};
        select_auction -> Result = auctions_core:select_auction(Id,Data), {reply,{ok,Result}, {Data,State}};
        auctions_list -> Result = auctions_core:auctions_list(Data), {reply,{ok,Result}, {Data,State}};
        auctions_agent_list -> Result = auctions_core:auctions_agent_list(Data, Payload), {reply,{ok,Result}, {Data,State}};
        make_bid -> {NewData, Result} = auctions_core:make_bid({Command,Id,Payload}, {Data,State}), {reply, {ok,Result}, {NewData, State}};
        delete_bid -> {NewData, Result} = auctions_core:delete_bid({Command,Id,Payload}, {Data,State}), {reply, {ok,Result}, {NewData, State}}
    end.
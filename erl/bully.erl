-module(bully).

-export([handle_leader_timeout/1, handle_ok_timeout/1]).
-export([handle_hello_message/2, handle_election_message/2, handle_ok_message/1, handle_leader_message/2]).
-export([initialization/1, start_election/2, send_hello_message/1, send_election_message/1, send_ok_message/1, send_leader_message/1]).
-export([random_leader_timeout/0, higher_ids/1, lower_ids/1]).

-define(MAX_LEADER_TIMEOUT, 10000).
-define(MIN_LEADER_TIMEOUT, 5000).
-define(OK_TIMEOUT, 5000).

-record(state, {cluster, bully_state, leader}).

%-----------------------------------------------------------------------------------------------------------------------------------------------------------
random_leader_timeout()->
    Levels = ((?MAX_LEADER_TIMEOUT - ?MIN_LEADER_TIMEOUT)/1000) + 1,
    Random = rand:uniform(round(Levels)) - 1,
    ?MIN_LEADER_TIMEOUT + (Random*1000).

higher_ids(Pids) ->
  lists:filter(fun(Pid) -> Pid > self() end, Pids).

lower_ids(Pids) ->
  lists:filter(fun(Pid) -> Pid < self() end, Pids).

%---------------------------------------------------------------------------------------------------------------------------------------------------------------

initialization(Cluster) ->
    State = #state{cluster = Cluster, bully_state = hello, leader = ""},
    bully:send_hello_message(Cluster),
    erlang:send_after(bully:random_leader_timeout() , self(), leader_timeout),
    State.

start_election(Cluster, State) ->
    %io:format("~p : start election\n", [self()]),
    Pids = higher_ids(utility:pids_from_global_registry("e_" ++ integer_to_list(Cluster))),
    if 
        length(Pids) == 0 -> send_leader_message(Cluster), NewState = State#state{bully_state = leader, leader=self()}, NewState;
        true -> send_election_message(Pids), erlang:send_after(?OK_TIMEOUT, self(), ok_timeout), NewState = State#state{bully_state = election}, NewState
    end.

send_hello_message(Cluster) ->
    %io:format("~p : send hello message\n", [self()]),
    Pids = utility:pids_from_global_registry("e_" ++ integer_to_list(Cluster)),
    utility:cast(Pids, {hello_message, self()}).

send_election_message(Pids) ->
    %io:format("~p : send election messages\n", [self()]),
    utility:cast(Pids, {election_message, self()}).

send_ok_message(Dest) ->
    utility:cast(Dest, ok_message).

send_leader_message(Cluster) ->
    %io:format("~p : Auto-elected as leader\n", [self()]),
    Pids = utility:pids_from_global_registry("e_" ++ integer_to_list(Cluster)) ++ utility:pids_from_global_registry("d_"),
    utility:cast(Pids, {leader_message,Cluster, self()}).


%--------------------------------------------------------------------------------------------------------------------------------------------------------------------

handle_leader_timeout(State) ->
    %io:format("~p : leader_timeout expired, my state is ~p\n", [self(), State]),
    erlang:send_after(random_leader_timeout(), self(), leader_timeout),
    if 
        State#state.bully_state == slave -> NewState = State#state{bully_state = hello},
                                            utility:cast([State#state.leader], {hello_message, self()});
        State#state.bully_state == hello -> NewState = start_election(State#state.cluster, State);
        true -> NewState = State

    end,
    NewState.
    
    
handle_ok_timeout(State) -> 
    if 
        State#state.bully_state =/= wait_leader_message -> NewState = State#state{bully_state = leader}, 
                                                            bully:send_leader_message(State#state.cluster), 
                                                            NewState;
        true -> State
    end.

handle_hello_message(From, State) ->
    %io:format("~p : received hello message, my state is ~p\n", [self(), State]),
    if 
        State#state.leader == self() -> io:format("~p : i'm leader\n", [self()]), utility:cast([From],{leader_message,State#state.cluster, self()});
        true -> ok
    end,
    State.

handle_election_message(From, State) ->
    send_ok_message(From),
    if
        (State#state.bully_state == election) or (State#state.bully_state == leader)  ->  State;
        true -> NewState = bully:start_election(State#state.cluster, State), NewState
    end.

handle_ok_message(State) ->
    NewState = State#state{bully_state = wait_leader_message},
    NewState.

handle_leader_message(Leader, State) ->
    %io:format("~p : received leader message\n", [self()]),
    NewState = State#state{bully_state = slave, leader = Leader},
    NewState.

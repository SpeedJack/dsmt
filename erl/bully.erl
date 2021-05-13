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
%returns a random number of seconds between MIN_LEADER_TIMEOUT and MAX_LEADER_TIMEOUT
random_leader_timeout()->
    Levels = ((?MAX_LEADER_TIMEOUT - ?MIN_LEADER_TIMEOUT)/1000) + 1,
    Random = rand:uniform(round(Levels)) - 1,
    ?MIN_LEADER_TIMEOUT + (Random*1000).

%returns pids higher than the process pid
higher_ids(Pids) ->
  lists:filter(fun(Pid) -> Pid > self() end, Pids).

%returns pids lower than the process pid
lower_ids(Pids) ->
  lists:filter(fun(Pid) -> Pid < self() end, Pids).

%---------------------------------------------------------------------------------------------------------------------------------------------------------------

%initialization of process for the bully algorithm of the cluster 'Cluster':
%Process sends a hello message to all the processes of the same cluster to which it belongs
% and waits in case a 'leader message' arrives from the leader. 
initialization(Cluster) ->
    State = #state{cluster = Cluster, bully_state = hello, leader = ""},
    bully:send_hello_message(Cluster),
    erlang:send_after(bully:random_leader_timeout() , self(), leader_timeout),
    State.

%Process start a new election procedure sending 'election message' to all the processes with higher pids of the same cluster to which it belongs 
%and wait for at least one 'ok message'
start_election(Cluster, State) ->
    %io:format("~p : start election\n", [self()]),
    Pids = higher_ids(utility:pids_from_global_registry("e_" ++ integer_to_list(Cluster))),
    send_election_message(Pids), 
    erlang:send_after(?OK_TIMEOUT, self(), ok_timeout), NewState = State#state{bully_state = election},
    NewState.
 
%Process sends a hello message to all the processes of the same cluster to which it belongs
send_hello_message(Cluster) ->
    %io:format("~p : send hello message\n", [self()]),
    Pids = utility:pids_from_global_registry("e_" ++ integer_to_list(Cluster)),
    utility:cast(Pids, {hello_message, self()}).

%Process sends a 'election message' to all the processes with higher pids of the same cluster to which it belongs 
send_election_message(Pids) ->
    %io:format("~p : send election messages to ~p\n", [self(), Pids]),
    utility:cast(Pids, {election_message, self()}).

%Process sends a 'ok message' to the destination 'Dest'
send_ok_message(Dest) ->
    %io:format("~p : send ok message to ~p\n", [self(), Dest]),
    utility:cast([Dest], ok_message).

%Process sends a 'leader message' to all the processes with lower pids of the same cluster to which it belongs
send_leader_message(Cluster) ->
    %io:format("~p : Auto-elected as leader\n", [self()]),
    Pids = utility:pids_from_global_registry("e_" ++ integer_to_list(Cluster)) ++ utility:pids_from_global_registry("d_"),
    utility:cast(Pids, {leader_message,Cluster, self()}).


%--------------------------------------------------------------------------------------------------------------------------------------------------------------------

%Each time a 'leader timeout' self-message is received, the same self-message is sent. 
%Then, if the process is in the 'slave' state, a 'hello_message' is sent to the leader;
%if the process is in the 'hello' state, it means that the leader is no longer reachable and therefore a new election is started.
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
    
%Each time a 'ok timeout' self-message is received, if the process is in the 'election' state, it means that
%no process with an higher pid has replied to an 'election message' sent before. 
%Therefore, the process elects itself as a leader. 
handle_ok_timeout(State) -> 
    %io:format("~p : ok_timeout expired, my state is ~p\n", [self(), State]),
    if 
        State#state.bully_state == election -> NewState = State#state{bully_state = leader, leader=self()}, 
                                                bully:send_leader_message(State#state.cluster), 
                                                NewState;
        true -> State
    end.

%Each time a 'hello_message' is received, if the process is the leader, it sends back a 'leader_message'.
handle_hello_message(From, State) ->
    %io:format("~p : received hello message, my state is ~p\n", [self(), State]),
    if 
        State#state.leader == self() -> %io:format("~p : i'm leader\n", [self()]), 
                                        utility:cast([From],{leader_message,State#state.cluster, self()});
        true -> ok
    end,
    State.

%Each time a 'election_message' is received, if the process is is in the 'election' state or in the 'hello' state,
% it starts a new election.
handle_election_message(From, State) ->
    send_ok_message(From),
    if
        (State#state.bully_state == slave) or (State#state.bully_state == hello)  ->  NewState = bully:start_election(State#state.cluster, State), NewState;
        true -> State
    end.

%Each time a 'ok_message' is received, if the process is is in the 'election' state, it move to 'wait_leader_message' state and it waits a 'leader_message'.
handle_ok_message(State) ->
    %io:format("~p : received ok message\n", [self()]),
    if 
        State#state.bully_state == election -> NewState = State#state{bully_state = wait_leader_message}, NewState;
        true -> State
    end.

%Each time a 'leader_message' is received, the process sets the receiving pids as the pid of the leader.
handle_leader_message(Leader, State) ->
    %io:format("~p : received leader message\n", [self()]),
    NewState = State#state{bully_state = slave, leader = Leader},
    NewState.

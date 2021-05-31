-module(das).

-export([start_d/2, stop_d/2,start_dispatchers/2, stop_dispatchers/2]).
-export([start_e/2, stop_e/2, start_cluster/4, stop_cluster/4, start_executors/2, stop_executors/2]).
-export([start_system/0, stop_system/0]).

-define(NAME_DISP_NODE, "disp").
-define(NAME_EXEC_NODE, "exec").

-define(NUM_DISPATCHERS_PER_NODE, 1).
-define(NUM_CLUSTER, 1).
-define(NUM_PROCESSOR_PER_CLUSTER,3).




%----- INITIALIZATION DISPATCHER --------------------------------------------------------------------
start_d(_, 0) -> ok;
start_d(Start, Num) ->
  Name = "d_" ++ integer_to_list(Start+Num-1) ++ "_" ++ atom_to_list(node()),
  gen_server:start({global, Name}, dispatcher, [], []),
  start_d(Start, Num - 1).

stop_d(_ , 0) -> ok;
stop_d(Start, Num)->
    Name = "d_" ++ integer_to_list(Start+Num-1) ++ "_" ++ atom_to_list(node()),
    gen_server:stop({global, Name}),
    stop_d(Start, Num - 1).

start_dispatchers([], _) -> ok;
start_dispatchers([H|T], StartName)->
  rpc:call(H,das,start_d,[StartName,?NUM_DISPATCHERS_PER_NODE]),
  start_dispatchers(T,StartName+?NUM_DISPATCHERS_PER_NODE).

stop_dispatchers([], _) -> ok;
stop_dispatchers([H|T], StartName)->
  rpc:call(H,das,stop_d,[StartName,?NUM_DISPATCHERS_PER_NODE]),
  stop_dispatchers(T,StartName+?NUM_DISPATCHERS_PER_NODE).

%------ INITIALIZATION EXECUTOR -----------------------------------------------------------------------

start_e(Cluster, IdName) ->
  Name = "e_" ++ integer_to_list(Cluster) ++ "_" ++ atom_to_list(node()) ++ "_" ++ integer_to_list(IdName),
  gen_server:start({global, Name}, executor, Cluster, []).

stop_e(Cluster, IdName)->
    Name = "e_" ++ integer_to_list(Cluster) ++ "_" ++ atom_to_list(node()) ++ "_" ++ integer_to_list(IdName),
    gen_server:stop({global, Name}).

start_cluster(_, _,_,0) -> ok;
start_cluster(Nodes, Cluster, Index, IdName) ->
  io:format("Nodi: ~p, Cluster ~p, Index ~p, IdName ~p\n", [Nodes, Cluster,Index, IdName]),
  Node = lists:nth(Index, Nodes),
  NewIndex = Index rem length(Nodes) + 1,
  rpc:call(Node,das, start_e,[Cluster,IdName]),
  start_cluster(Nodes, Cluster, NewIndex, IdName-1).


stop_cluster([], _,_,0) -> ok;
stop_cluster(Nodes, Cluster, Index, IdName) ->
  Node = lists:nth(Index, Nodes),
  NewIndex = Index rem length(Nodes) + 1,
  rpc:call(Node,das, stop_e,[Cluster,IdName]),
  stop_cluster(Nodes, Cluster, NewIndex, IdName-1).

start_executors(_, 0)->ok;
start_executors(Nodes,Num) ->
  start_cluster(Nodes,Num,1,?NUM_PROCESSOR_PER_CLUSTER),
  start_executors(Nodes,Num-1).

stop_executors(_, 0)->ok;
stop_executors(Nodes,Num) ->
  stop_cluster(Nodes,Num,1,?NUM_PROCESSOR_PER_CLUSTER),
  stop_executors(Nodes,Num-1).


%-------- INITIALIZATION SYSTEM -------------------------------------------------------------------------

start_system() ->
  Nodes = nodes(connected) ++ [node()],
  store:initialization(Nodes),
  start_dispatchers(lists:filter(fun(N) -> utility:starts_with(atom_to_list(N),?NAME_DISP_NODE)end,Nodes), 1),
  start_executors(lists:filter(fun(N) -> utility:starts_with(atom_to_list(N),?NAME_EXEC_NODE) end,Nodes),?NUM_CLUSTER).

stop_system() ->
  Nodes = nodes(connected) ++ [node()],
  stop_dispatchers(Nodes, 1),
  stop_executors(Nodes,?NUM_CLUSTER).



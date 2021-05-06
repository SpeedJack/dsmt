-module(prova).

-export([start_d/2, stop_d/2, start_e/3, stop_e/3, start_cluster/1, start_system/0]).

-define(NUM_DISPATCHERS, 3).
-define(NUM_CLUSTER, 1).
-define(NUM_REPLICA_PER_CLUSTER,3).

%----- INITIALIZATION DISPATCHER --------------------------------------------------------------------
start_d(_, 0) -> ok;
start_d(Start, Num) ->
  Name = "d_" ++ integer_to_list(Start+Num-1),
  gen_server:start({global, Name}, dispatcher, [], []),
  start_d(Start, Num - 1).

stop_d(_ , 0) -> ok;
stop_d(Start, Num)->
    Name = "d_" ++ integer_to_list(Start+Num-1),
    gen_server:stop({global, Name}),
    stop_d(Start, Num - 1).

%------ INITIALIZATION EXECUTOR -----------------------------------------------------------------------
start_e(_, 0, _) -> ok;
start_e(Start, Num, Cluster) ->
  Name = "e_" ++ integer_to_list(Cluster) ++ "_" ++ integer_to_list(Start+Num-1),
  gen_server:start({global, Name}, executor, 1, []),
  start_e(Start, Num - 1, Cluster).

stop_e(_ , 0, _) -> ok;
stop_e(Start, Num, Cluster)->
    Name = "e_" ++ integer_to_list(Cluster) ++ "_" ++ integer_to_list(Start+Num-1),
    gen_server:stop({global, Name}),
    stop_e(Start, Num - 1, Cluster).

start_cluster(0) -> ok;
start_cluster(Num) ->
  start_e(1,?NUM_REPLICA_PER_CLUSTER, Num),
  start_cluster(Num-1).


start_system() ->
  start_d(1,?NUM_DISPATCHERS),
  start_cluster(?NUM_CLUSTER).




-module(prova).


-export([start_e/2, stop_e/2, prova/0]).

-record(state, {cluster, bully_state, leader}).

start_e(_, 0) -> ok;
start_e(Start, Num) ->
  Name = "e_1_" ++ integer_to_list(Start+Num-1),
  gen_server:start({global, Name}, executor, 1, []),
  start_e(Start, Num - 1).

stop_e(_ , 0) -> ok;
stop_e(Start, Num)->
    Name = "e_1_" ++ integer_to_list(Start+Num-1),
    gen_server:stop({global, Name}),
    stop_e(Start, Num - 1).


prova() ->
  State = #state{cluster =1, bully_state= hello, leader=4}.


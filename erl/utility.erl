-module(utility).
-export([starts_with/2 ]).
-export([extract_by_index/2, modify_key_value_list/2]).
-export([pids_from_global_registry/1]).
-export([call/2, call/3, cast/2]).

%----------------STRING--------------------------------------------------

%return true if String starts with Start, otherwise false
starts_with(String, Start) ->
    Len = string:len(Start),
    Substring = string:sub_string(String, 1, Len),
    if
        Substring == Start -> true;
        Substring =/= Start -> false
    end.

%----------------- LIST ----------------------------------------------

%check if the key of the new tuple is in list. if it is in list modify the value of the tuple,
%otherwise add the tuple to the list
modify_key_value_list(List,{Key,Value}) ->
    Result = lists:keysearch(Key,1,List),
    if 
        Result == false -> NewList = List ++ [{Key,Value}];
        true -> NewList = lists:keyreplace(Key,1,List,{Key,Value})
    end,
    NewList.

%extract the ith value in the list
extract_by_index([H|T], Index)->
    if
        Index == 1 -> H;
        Index > 1 -> extract_by_index(T, Index -1);
        Index < 1 -> error
    end .
%------------------GLOBAL REGISTRY ----------------------------------------------

pids_from_global_registry([],_) -> [];
pids_from_global_registry([H|T], Start) ->
    Result = starts_with(H, Start),
    Pid = global:whereis_name(H),
    if
        (Result == true) and (self() =/= Pid) -> [Pid] ++ pids_from_global_registry(T, Start);
        true -> [] ++ pids_from_global_registry(T,Start)
    end.

pids_from_global_registry(Selector)->
    pids_from_global_registry(global:registered_names(), Selector).


%------------------- MESSAGES -------------------------------------------------------
cast([],_) -> null;
cast([HeadPid|T], Message) ->
    if 
        HeadPid =/= self() -> gen_server:cast(HeadPid, Message);
        true -> ok
    end,
    cast(T, Message).


call(Dest,Msg, Timeout) ->
    try gen_server:call(Dest, Msg, Timeout)
    catch
        exit:{Error,_} -> {error,Error}
    end.
call(Dest, Msg) ->
    call(Dest, Msg, 5000).
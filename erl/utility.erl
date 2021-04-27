-module(utility).
-export([starts_with/2 ]).
-export([extract_by_index/2, extract_value_from_key/2, modify_key_value_list/2, delete_element_from_key_value_list/2]).
-export([pids_from_global_registry/1]).
-export([handle_call/2, handle_call/3]).
-export([send_message/2]).

%----------------STRING--------------------------------------------------

%return true if String starts with Start, otherwise false
starts_with(String, Start) ->
    Len = string:len(Start),
    Substring = string:sub_string(String, 1, Len),
    if
        Substring == Start -> true;
        Substring =/= Start -> false
    end.
%-----------------HANDLE LIST ----------------------------------------------
%exctract the value of the key-value pair in the list, if present
extract_value_from_key(List, Key) ->
    Tuple = lists:keyfind(Key,1,List),
    if
        Tuple =/= false -> {_,V} = Tuple, V;
        Tuple == false -> false
    end .

%check if the key of the new tuple is in list. if it is in list modify the value of the tuple,
%otherwise add the tuple to the list
modify_key_value_list(List,{Key,Value}) ->
    Result = lists:keysearch(Key,1,List),
    if 
        Result == false -> NewList = List ++ {Key,Value};
        true -> NewList = lists:keyreplace(Key,1,List,{Key,Value})
    end,
    NewList.

delete_element_from_key_value_list(List, Key) ->
    lists:keydelete(Key, 1, List).


%exctract the value by index in the list
extract_by_index([H|T], Index)->
    if
        Index == 1 -> H;
        Index > 1 -> extract_by_index(T, Index -1);
        Index < 1 -> error
    end .
%------------------HANDLE EXCEPTION ---------------------------------------------
handle_call(Dest,Msg, Timeout) ->
    try gen_server:call(Dest, Msg, Timeout)
    catch
        exit:{Error,_} -> Error
    end.
handle_call(Dest, Msg) ->
    handle_call(Dest, Msg, 0).

%------------------HANDLE REGISTRY ----------------------------------------------

pids_from_global_registry([],_) -> [];
pids_from_global_registry([H|T], Start) ->
    Result = starts_with(H, Start),
    if
        Result == true -> [global:whereis_name(H)] ++ pids_from_global_registry(T, Start);
        Result == false -> [] ++ pids_from_global_registry(T,Start)
    end.

pids_from_global_registry(Selector)->
    pids_from_global_registry(global:registered_names(), Selector).


%------------------- MESSAGES -------------------------------------------------------
send_message([],_) -> null;
send_message([HeadPid|T], Message) ->
    if 
        HeadPid =/= self() -> gen_server:cast(HeadPid, Message);
        true -> ok
    end,
    send_message(T, Message).
-module(utility).
-export([starts_with/2, obtain_names_from_registry/2]).

starts_with(String, Start) ->
    Len = string:len(Start),
    Substring = string:sub_string(String, 1, Len),
    if
        Substring == Start -> true;
        Substring =/= Start -> false
    end.

obtain_names_from_registry(local, dispatcher) ->
    obtain_names_from_registry(registered(), "dispatcher_");
     
obtain_names_from_registry(global, dispatcher) ->
    Names = global:registered_names(),
    obtain_names_from_registry(Names, "dispatcher_");

obtain_names_from_registry(local, auctions_core) ->
    obtain_names_from_registry(registered(), "auctions_core_");

obtain_names_from_registry(global, auctions) ->
    obtain_names_from_registry(global:registered_names(), "auctions_core_");

obtain_names_from_registry([],_) -> [];
obtain_names_from_registry([H|T], Start) ->
    Result = starts_with(H, Start),
    if
        Result == true -> [H] ++ obtain_names_from_registry(T, Start);
        Result == false -> [] ++ obtain_names_from_registry(T,Start)
    end.


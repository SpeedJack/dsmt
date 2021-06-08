-module(auctions_core).

-export([send_to_slaves/2]).
-export([compute_auction_state/2, select_winning_bids/3, compare_bids/2]).
-export([create_auction/2, delete_auction/2, select_auction/2, get_auction_state/2, auctions_list/1, auctions_agent_list/1, auctions_bidder_list/1, make_bid/2, delete_bid/2]).

-record(state, {cluster, bully_state, leader}).


%---------------------Structure of DATA in executor -----------------------------------
%   Data = [{IdAuction1, AuctionState1}, ....., {IdAuctionN, AuctionStateN}]
%   AuctionI = {AuctionData, BidList, AuctionState}
%   AuctionState = [Bidx,...,Bidy] list of the actual winning bids
%   Bid = {id_bid, id_user, timestamp,bid_value, quantity} a bid
%---------------------------------------------------------------------------------------

%used by the cluster's leader to forward AuctionState changes to slaves
send_to_slaves([], _) -> null;
send_to_slaves([HeadPid|T], Message) ->
    utility:call(Message, HeadPid,0),
    send_to_slaves(T, {leader_update, '_', Message}).


%Compare two bids to understand how to order them: 
%A bid A is worse than bid B if 'bid_value' of A is less than 'bid_value' of B 
%or if the A and B's 'bid_value' are equal, but 'timestamp' of A is greater than'timestamp' of B.
compare_bids(ElemA, ElemB) ->
    (element(5,ElemA) < element(5,ElemB)) or 
    ((element(5,ElemA) == element(5,ElemB)) and (element(4, ElemA) > element(4, ElemB))).

%The method calculates the new AuctionState by sorting the bids by value and timestamp.
% The method makes sure that winning bids are unique to each user.
select_winning_bids(_,_,[]) -> [];
select_winning_bids(SaleQuantity, WinningUsers, [HeadSorted|T]) ->
    User = element(3,HeadSorted),
    Check = lists:member(User, WinningUsers),
    if 
        Check == false ->  NewWinningUsers = WinningUsers ++ [User];
        true -> NewWinningUsers = WinningUsers, select_winning_bids(SaleQuantity, WinningUsers, T)
    end,

    BidQuantity = element(6,HeadSorted),
    if 
        SaleQuantity - BidQuantity > 0 -> [HeadSorted] ++ select_winning_bids(SaleQuantity-BidQuantity, NewWinningUsers, T);
        SaleQuantity - BidQuantity == 0 -> [HeadSorted];
        true -> [] ++ select_winning_bids(SaleQuantity, NewWinningUsers, T)
    end.

%Computes a new state for an auction. This is done whenever a bid for an auction is added or deleted.
compute_auction_state(AuctionData, BidList) ->
    SaleQuantity = element(9,AuctionData),
    SortedBidList = lists:reverse(lists:sort(fun compare_bids/2, BidList)),
    io:format("SortedBidList ~p\n", [SortedBidList]),
    Result = select_winning_bids(SaleQuantity,[], SortedBidList),
    Result.
%-----------------------------------------------------------------------------------------------------

%Creates a new auction by saving it in Mnesia and adding it to the auctions that the cluster manages. 
%If the process is the leader, it forwards the new AuctionState to the slaves.
create_auction(Message, {Data, State}) ->
    {_, Id, NewAuction} = Message,
    {Code, Res} = store:insert_auction(NewAuction),
    if
        (Code == atomic) and (self() == State#state.leader) ->  NewData = Data ++ [{Id, []}],
                                                            send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData),
                                                            {{ok,ok}, NewData};
        (Code == atomic) and (self() =/= State#state.leader) -> NewData = Data ++ [{Id, []}], 
                                                            {{ok,ok}, NewData};
        true -> {{err,Res}, Data}
    end.
%Deletes an auction from Mnesia and remove it from the auctions that the cluster manages. 
%If the process is the leader, it forwards the new AuctionState to the slaves.
delete_auction(Message, {Data, State}) ->
    {_, Id, _} = Message,
    {Code,Res} = store:delete_auction(Id),
    {atomic, Bids} = store:get_bid_list(Id),
    [store:delete_bid(element(1,Bid))|| Bid <- Bids],
    if
        (Code == atomic) and (self() == State#state.leader) ->  NewData = lists:keydelete(Id, 1, Data), 
                                                            send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData),
                                                            {{ok,ok}, NewData};
        (Code == atomic) and (self() =/= State#state.leader) -> NewData = lists:keydelete(Id, 1, Data),
                                                            {{ok,ok}, NewData};
        true -> {{err,Res}, Data}
    end.

%Obtains from Mnesia the data of an auction and the list of bids made by the user who requested the auction
select_auction(AuctionId, UserId) ->
    {Code1, Auction} = store:get_auction(AuctionId),
    {Code2, BidList} = store:get_bid_list(AuctionId, UserId),
    if 
        (Code1 == atomic) and (Code2 == atomic) -> {ok,{Auction, BidList}};
        Code1 =/= atomic -> {err, Auction};
        true -> {err, BidList}
    end.

%Obtains the data of an auction state
get_auction_state(AuctionId, {Data, _}) ->
    Tuple = lists:keyfind(AuctionId, 1, Data),
    if
        Tuple == false -> 
            {Code1,Auction} = store:get_auction(AuctionId),
            {Code2, BidList} = store:get_bid_list(AuctionId),
            if 
                (Code1 == atomic) and (Code2 == atomic) -> AuctionState = compute_auction_state(Auction, BidList), {ok, AuctionState};
                true -> {ok,[]}
            end;

        true -> {_, List} = Tuple, {ok,List} 
    end.
        
%Inserts a new bid in Mnesia and calculates the auction's new AuctionState
make_bid(Message, {Data, State}) ->
    {_, Id, NewBid} = Message,
    {Code1,Auction} = store:get_auction(Id),
    if 
        (Code1 == atomic) and (element(6,Auction) > element(4,NewBid)) ->
            {Code2, Res} = store:insert_bid(NewBid, Id),
            {Code3, BidList} = store:get_bid_list(Id),
            if 
                (Code2 == atomic) and (Code3 == atomic) -> 
                    NewAuctionState = compute_auction_state(Auction, BidList),
                    NewData = utility:modify_key_value_list(Data, {Id, NewAuctionState}),
                    %io:format("AUCTIION STATE: ~p\n", [NewData]),                                   
                    if
                        self() == State#state.leader -> send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData);
                        true -> null
                    end,
                    {{ok,NewAuctionState}, NewData};
                Code2 =/= atomic -> 
                    {{err,Res}, Data};
                true -> 
                    {{err,BidList}, Data}
            end;
        Code1 =/= atomic -> 
            {{err, Auction}, Data};
        true -> 
            {{err,time_expired},Data}
    end.
    

%Delets a bid from Mnesia and calculates the auction's new AuctionState
delete_bid(Message, {Data, State}) ->
    {_, Id, IdBid} = Message,
    {Code1,Auction} = store:get_auction(Id),
    if 
        (Code1 == atomic) ->
            {Code2, Res} = store:delete_bid(IdBid),
            {Code3, BidList} = store:get_bid_list(Id),
            if 
                (Code2 == atomic) and (Code3 == atomic) ->    
                    NewAuctionState = compute_auction_state(Auction, BidList),
                    NewData = utility:modify_key_value_list(Data, {Id, NewAuctionState}), 
                    if
                        self() == State#state.leader -> send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData);
                        true -> null
                    end,
                    {{ok,NewAuctionState}, NewData};
                Code2 =/= atomic -> 
                    {{err,Res}, Data};
                true =/= atomic -> 
                    {{err,BidList}, Data}
            end;
        Code1 =/= atomic -> 
            {{err, Auction}, Data};
        true -> 
            {{err,time_expired},Data}
    end.

%Returns the auction list
auctions_list(Page) ->
    {Code,AuctionList} = store:get_auction_list(Page),
    if
        Code == atomic -> {ok, AuctionList};
        true -> {err, AuctionList}
    end.

%returns the list of auctions in which the user has made at least 1 bid
auctions_bidder_list(IdBidder) ->
    {Code, AuctionList} = store:get_bidder_auctions(IdBidder),
    if
        Code == atomic -> {ok, AuctionList};
        true -> {err, AuctionList}
    end.

%returns the list of auctions created by an agent
auctions_agent_list(AgentId) ->
    {Code, AuctionList} = store:get_agent_auctions(AgentId),
    if
        Code == atomic -> {ok, AuctionList};
        true -> {err, AuctionList}
    end.

-module(auctions_core).

-export([send_to_slaves/2]).
-export([compute_auction_state/2, select_winning_bids/3, compare_bids/2]).
-export([create_auction/2, delete_auction/2, select_auction/2, auctions_list/0, auctions_agent_list/1, auctions_bidder_list/1, make_bid/2, delete_bid/2]).

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
    element(4,ElemA) < element(4,ElemB) or 
    ((element(4,ElemA) == element(4,ElemB)) and (element(3, ElemA) > element(3, ElemB))).

%The method calculates the new AuctionState by sorting the bids by value and timestamp.
% The method makes sure that winning bids are unique to each user.
select_winning_bids(_,_,[]) -> [];
select_winning_bids(SaleQuantity, WinningUsers, [HeadSorted|T]) ->
    User = element(2,HeadSorted),
    Check = lists:member(User, WinningUsers),
    if 
        Check == false ->  NewWinningUsers = WinningUsers ++ [User];
        true -> NewWinningUsers = WinningUsers, select_winning_bids(SaleQuantity, WinningUsers, T)
    end,

    BidQuantity = element(5,HeadSorted),
    if 
        SaleQuantity - BidQuantity > 0 -> [HeadSorted] ++ select_winning_bids(SaleQuantity-BidQuantity, NewWinningUsers, T);
        SaleQuantity - BidQuantity == 0 -> [HeadSorted];
        true -> NewHeadSorted =setelement(5, HeadSorted, SaleQuantity), [NewHeadSorted]
    end.

%Computes a new state for an auction. This is done whenever a bid for an auction is added or deleted.
compute_auction_state(AuctionData, BidList) ->
    SaleQuantity = element(8,AuctionData),
    SortedBidList = lists:reverse(lists:sort(fun compare_bids/2, BidList)),
    Result = select_winning_bids(SaleQuantity,[], SortedBidList),
    Result.
%-----------------------------------------------------------------------------------------------------

%Creates a new auction by saving it in Mnesia and adding it to the auctions that the cluster manages. 
%If the process is the leader, it forwards the new AuctionState to the slaves.
create_auction(Message, {Data, State}) ->
    {_, Id, NewAuction} = Message,
    NewData = Data ++ [{Id, []}],
    store:insert_auction(NewAuction),
    if
        self() == State#state.leader -> send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData);
        true -> null
    end,
    NewData.

%Deletes an auction from Mnesia and remove it from the auctions that the cluster manages. 
%If the process is the leader, it forwards the new AuctionState to the slaves.
delete_auction(Message, {Data, State}) ->
    {_, Id, _} = Message,
    NewData = lists:keydelete(Id, 1, Data),
    store:delete_auction(Id),
    if
        self() == State#state.leader -> send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData);
        true -> null
    end,
    NewData.

%Obtains from Mnesia the data of an auction and the list of bids made by the user who requested the auction
select_auction(AuctionId, UserId) ->
    Auction = store:get_auction(AuctionId),
    BidList = store:get_bid_list(AuctionId, UserId),
    {Auction, BidList}.

%Inserts a new bid in Mnesia and calculates the auction's new AuctionState
make_bid(Message, {_, State}) ->
    {_, Id, NewBid} = Message,
    store:insert_bid(NewBid, Id),
    Auction = store:get_auction(Id),
    BidList = store:get_bid_list(Id),
    NewData = compute_auction_state(Auction, BidList),
    %io:format("NEW ACTION STATE ~p\n", [NewData]),
    if
        self() == State#state.leader -> send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData);
        true -> null
    end,
    NewData.

%Delets a bid from Mnesia and calculates the auction's new AuctionState
delete_bid(Message, {_, State}) ->
    {_, Id, IdBid} = Message,
    store:delete_bid(IdBid),
    Auction = store:get_auction(Id),
    BidList = store:get_bid_list(Id),
    NewData = compute_auction_state(Auction, BidList),
    if
        self() == State#state.leader -> send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData);
        true -> null
    end,
    NewData.

%Returns the auction list
auctions_list() ->
    AuctionList = store:get_auction_list(),
    AuctionList.

%returns the list of auctions in which the user has made at least 1 bid
auctions_bidder_list(BidderList) ->
    AuctionList = store:get_bidder_auctions(BidderList),
    AuctionList.

%returns the list of auctions created by an agent
auctions_agent_list(AgentId) ->
    AuctionList = store:get_agent_auctions(AgentId),
    AuctionList.

-module(auctions_core).

-export([send_to_slaves/2]).
-export([compute_auction_state/2, select_winning_bids/3, compare_bids/2]).
-export([create_auction/2, delete_auction/2, select_auction/2, auctions_list/1, auctions_agent_list/1, auctions_bidder_list/1, make_bid/2, delete_bid/2]).

-record(state, {cluster, bully_state, leader}).


%---------------------Structure of DATA in executor -----------------------------------
%   Data = [{IdAuction1, Auction1}, ....., {IdAuctionN, AuctionN}]
%   AuctionI = {AuctionData, BidList, AuctionState}
%   AuctionData = {id_auction, id_agent, name, image, description, end_date, min_price, min_raise, sale_quantity} auction data
%   BidList = [Bid1,...,BidM] list of all bids done for this auction
%   AuctionState = [Bidx,...,Bidy] list of the actual winning bids
%   Bid = {id_bid, id_user, timestamp,bid_value, quantity} a bid
%---------------------------------------------------------------------------------------

send_to_slaves([], _) -> null;
send_to_slaves([HeadPid|T], Message) ->
    utility:call(Message, HeadPid,0),
    send_to_slaves(T, {leader_update, '_', Message}).


compare_bids(ElemA, ElemB) ->
    element(4,ElemA) < element(4,ElemB) or 
    ((element(4,ElemA) == element(4,ElemB)) and (element(3, ElemA) > element(3, ElemB))).


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


compute_auction_state(AuctionData, BidList) ->
    SaleQuantity = element(8,AuctionData),
    SortedBidList = lists:reverse(lists:sort(fun compare_bids/2, BidList)),
    Result = select_winning_bids(SaleQuantity,[], SortedBidList),
    Result.
%-----------------------------------------------------------------------------------------------------


create_auction(Message, {Data, State}) ->
    {_, Id, NewAuction} = Message,
    NewData = Data ++ [{Id, []}],
    store:insert_auction(NewAuction),
    if
        self() == State#state.leader -> send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData);
        true -> null
    end,
    NewData.

delete_auction(Message, {Data, State}) ->
    {_, Id, _} = Message,
    NewData = lists:keydelete(Id, 1, Data),
    store:delete_auction(Id),
    if
        self() == State#state.leader -> send_to_slaves(utility:pids_from_global_registry("e_" ++ integer_to_list(State#state.cluster)), NewData);
        true -> null
    end,
    NewData.


select_auction(AuctionId, UserId) ->
    Auction = store:get_auction(AuctionId),
    BidList = store:get_auction(AuctionId, UserId),
    {Auction, BidList}.

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

auctions_list([]) -> [];
auctions_list([HeadData|T]) ->
    {Id,{AuctionData, _, _}} = HeadData,
    {_,IdAgent,Name,_,EndDate,_,_,_} = AuctionData,
    [{Id,IdAgent,Name,EndDate}] ++auctions_list(T).


auctions_bidder_list(BidderList) ->
    AuctionList = store:get_bidder_auctions(BidderList),
    AuctionList.

auctions_agent_list(AgentId) ->
    AuctionList = store:get_agent_auctions(AgentId),
    AuctionList.

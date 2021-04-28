-module(auctions_core).

-export([send_to_slaves/2]).
-export([compute_auction_state/2]).
-export([create_auction/2, delete_auction/2, select_auction/2, auctions_list/1, auctions_agent_list/2, make_bid/2, delete_bid/2]).

-record(state, {cluster, bully_state, leader}).


%---------------------Structure of DATA in executor -----------------------------------
%   Data = [{IdAuction1, Auction1}, ....., {IdAuctionN, AuctionN}]
%   AuctionI = {AuctionData, BidList, AuctionState}
%   AuctionData = {id_auction, id_agent, name, image, end_date, min_price, min_raise, sale_quantity} auction data
%   BidList = [Bid1,...,BidM] list of all bids done for this auction
%   AuctionState = [Bidx,...,Bidy] list of the actual winning bids
%   Bid = {id_auction,id_user, timestamp,bid_value, quantity} a bid
%---------------------------------------------------------------------------------------


send_to_slaves(Cluster, Message) ->
    Pids = utility:pids_from_global_registry("e_" ++ integer_to_list(Cluster)),
    utility:send_message(Pids, Message).


compute_auction_state(_,[]) -> [];
compute_auction_state(SaleQuantity, [HeadSorted|T]) ->
    BidQuantity = element(5,HeadSorted),
    if 
        SaleQuantity - BidQuantity > 0 -> [HeadSorted] ++ compute_auction_state(SaleQuantity-BidQuantity, T);
        SaleQuantity - BidQuantity == 0 -> [HeadSorted];
        SaleQuantity - BidQuantity < 0 -> NewHeadSorted =setelement(5, HeadSorted, SaleQuantity), [NewHeadSorted]
    end;
compute_auction_state(AuctionData, BidList) ->
    SaleQuantity = element(8,AuctionData),
    SortedBidList = lists:reverse(lists:keysort(4, BidList)),
    Result = compute_auction_state(SaleQuantity,SortedBidList),
    Result.
%-----------------------------------------------------------------------------------------------------


create_auction(Message, {State,Data}) ->
    {_, Id, NewAuction} = Message,
    NewData = Data ++ [{Id, {NewAuction,[],[]} }],
    send_to_slaves(State#state.cluster, Message),
    NewData.

delete_auction(Message, {State,Data}) ->
    {_, Id, _} = Message,
    NewData = lists:keydelete(Id, 1, Data),
    send_to_slaves(State#state.cluster, Message),
    NewData.


select_auction(Id, Data) ->
    Auction = utility:extract_value_from_key(Data, Id),
    {AuctionData,_,_} = Auction,
    AuctionData.

make_bid(Message, {State,Data}) ->
    {_, Id, NewBid} = Message,
    Auction = utility:extract_value_from_key(Data, Id),
    {AuctionData,BidList,_} = Auction,
    NewBidList = BidList ++ [NewBid],
    NewAuctionState = compute_auction_state(AuctionData, NewBidList),
    NewAuction = {AuctionData,NewBidList,NewAuctionState},
    NewData = utility:modify_key_value_list(Data, {Id, NewAuction}),
    send_to_slaves(State#state.cluster, Message),
    {NewData, NewAuctionState}.

delete_bid(Message, {State,Data}) ->
    {_, Id, IdBid} = Message,
    Auction = utility:extract_value_from_key(Data, Id),
    {AuctionData,BidList,_} = Auction,
    NewBidList = lists:keydelete(IdBid, 1, BidList),
    NewAuctionState = compute_auction_state(AuctionData, NewBidList),
    NewAuction = {AuctionData,NewBidList,NewAuctionState},
    NewData = utility:modify_key_value_list(Data, {Id, NewAuction}),
    send_to_slaves(State#state.cluster, Message),
    {NewData, NewAuctionState}.

auctions_list([]) -> [];
auctions_list([HeadData|T]) ->
    {Id,{AuctionData, _, _}} = HeadData,
    {_,IdAgent,Name,_,EndDate,_,_,_} = AuctionData,
    [{Id,IdAgent,Name,EndDate}] ++auctions_list(T).

auctions_agent_list([],_) -> [];
auctions_agent_list([HeadData|T],[IdToFind]) ->
    {Id,{AuctionData, _, _}} = HeadData,
    {_,IdAgent,Name,_,EndDate,_,_,_} = AuctionData,
    if 
        IdToFind == IdAgent -> [{Id,IdAgent,Name,EndDate}] ++auctions_list(T);
        true -> [] ++ auctions_list(T)
    end.
-module(auctions_core).

-export([send_to_slaves/2]).
-export([compute_auction_state/2]).
-export([create_auction/2, delete_auction/2, select_auction/2, make_bid/2]).

-record(state, {cluster, bully_state, leader}).

send_to_slaves(Cluster, Message) ->
    Pids = utility:pids_from_global_registry("e_" ++ integer_to_list(Cluster)),
    utility:send_message(Pids, Message).

compute_auction_state(Auction, BidList) -> ok.


create_auction(Message, {State,Data}) ->
    {_, Id, NewAuction} = Message,
    NewData = Data ++ [{Id,{{NewAuction,[]},[]}}],
    send_to_slaves(State#state.cluster, Message),
    NewData.

delete_auction(Message, {State,Data}) ->
    {_, Id, _} = Message,
    NewData = lists:keydelete(Id, 1, Data),
    send_to_slaves(State#state.cluster, Message),
    NewData.


select_auction(Id, Data) ->
    Auction = utility:extract_value_from_key(Data, Id),
    {{AuctionData,_},_} = Auction,
    AuctionData.

make_bid(Message, {State,Data}) ->
    {_, Id, NewBid} = Message,
    Auction = utility:extract_value_from_key(Data, Id),
    {{AuctionData,BidList},_} = Auction,
    NewBidList = BidList ++ [NewBid],
    NewAuctionState = compute_auction_state(AuctionData, NewBidList),
    NewAuction = {{AuctionData,NewBidList},NewAuctionState},
    NewData = utility:modify_key_value_list(Data, {Id, NewAuction}),
    send_to_slaves(State#state.cluster, Message),
    {NewData, NewAuctionState}.
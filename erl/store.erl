-module(store).

-export([print_table/1]).
-export([record_to_tuple/2, tuple_to_record/2]).
-export([initialization/0, insert_auction/1, insert_bid/2, delete_auction/1, delete_bid/1, get_auction/1, get_auction_data/1, get_bid_list/1]).

-record(auction, {id_auction, id_agent, name, image, end_date, min_price, min_raise, sale_quantity}).
-record(bid, {id_bid, id_auction, id_user, timestamp,bid_value, quantity}).

record_to_tuple(bid, Record) ->
    {Record#bid.id_bid,
    Record#bid.id_user,
    Record#bid.timestamp,
    Record#bid.bid_value,
    Record#bid.quantity};
record_to_tuple(auction, Record)->
    {Record#auction.id_auction,
    Record#auction.id_agent,
    Record#auction.name,
    Record#auction.image,
    Record#auction.end_date,
    Record#auction.min_price,
    Record#auction.min_raise,
    Record#auction.sale_quantity}.

tuple_to_record(bid, Tuple) ->
    Record = #bid{id_bid= element(1,Tuple),
                id_auction = '_',
                id_user= element(2,Tuple),
                timestamp= element(3,Tuple),
                bid_value= element(4,Tuple),
                quantity= element(5,Tuple)},
    Record;
tuple_to_record(auction, Tuple) ->
    Record = #auction{id_auction= element(1,Tuple),
                    id_agent = element(2,Tuple),
                    name= element(3,Tuple),
                    image= element(4,Tuple),
                    end_date= element(5,Tuple),
                    min_price= element(6,Tuple),
                    min_raise= element(7,Tuple),
                    sale_quantity= element(8,Tuple)},
    Record.

%--------------------------------------------------------------------------------------------------------------

initialization() ->
    mnesia:create_schema([node()]),
    mnesia:start(),
    mnesia:create_table(auction,[{attributes, record_info(fields, auction)}]),
    mnesia:create_table(bid, [{attributes, record_info(fields, bid)}]).


%--- INSERT --------------------------------------------------------------------------------------------------------
insert_auction(Auction) ->
    AuctionRecord = tuple_to_record(auction,Auction),
    Fun =   fun() ->
                mnesia:write(AuctionRecord)
            end,
    mnesia:transaction(Fun).

insert_bid(Bid, AuctionId) ->
    BidRecord = tuple_to_record(bid,Bid),
    BidRecord2 = BidRecord#bid{id_auction = AuctionId},
    Fun =   fun() ->
                mnesia:write(BidRecord2)
            end,
    mnesia:transaction(Fun).

%--- DELETE ----------------------------------------------------------------------------------------------------------

delete_auction(AuctionId) ->
    Fun =   fun() ->
                mnesia:delete(auction, AuctionId)
            end,
    mnesia:transaction(Fun).

delete_bid(BidId) ->
    Fun =   fun() ->
                mnesia:delete(bid, BidId, write)
            end,
    mnesia:transaction(Fun).

%--- GET -----------------------------------------------------------------------------------------------------------------
get_auction_data(AuctionId) ->
    Fun =   fun() ->
                mnesia:read(auction,AuctionId)
            end,
    {atomic,AuctionRecord} = mnesia:transaction(Fun),
    if 
        AuctionRecord == [] -> [];
        true -> [H|_] = AuctionRecord, record_to_tuple(auction, H)
    end.


get_bid_list(AuctionId) ->
    Fun =   fun() ->
                mnesia:match_object(bid, {bid, '_', AuctionId, '_', '_', '_', '_'}, read)
            end,
    {atomic, BidRecords} = mnesia:transaction(Fun),
    if 
        BidRecords == [] -> [];
        true -> [record_to_tuple(bid,BidRecord) || BidRecord <- BidRecords]
    end.

get_auction(AuctionId) ->
    AuctionData = get_auction_data(AuctionId),
    BidList = get_bid_list(AuctionId),
    {AuctionId,{AuctionData, BidList, []}}.

%--- PRINT TABLE -------------------------------------------------------------------------------------------------
print_table(Table_name)->
    Iterator =  fun(Rec,_)->
                    io:format("~p~n",[Rec]),
                    []
                end,
    case mnesia:is_transaction() of
        true -> mnesia:foldl(Iterator,[],Table_name);
        false -> 
            Exec = fun({Fun,Tab}) -> mnesia:foldl(Fun, [],Tab) end,
            mnesia:activity(transaction,Exec,[{Iterator,Table_name}],mnesia_frag)
    end.



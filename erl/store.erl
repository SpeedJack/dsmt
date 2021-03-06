-module(store).

-export([print_table/1]).
-export([record_to_tuple/2, tuple_to_record/2]).
-export([initialization/1, start_nodes/1, stop_nodes/1]).
-export([insert_auction/1, insert_bid/2]).
-export([delete_auction/1, delete_bid/1]).
-export([get_auction/1, get_auction_list/1, get_bid_list/1, get_bid_list/2, get_agent_auctions/1, get_bidder_auctions/1]).

-define(PAGE_SIZE, 50).

-record(auction, {id_auction, id_agent, name, image, description, end_date, min_price, min_raise, sale_quantity}).
-record(bid, {id_bid, id_auction, id_user, timestamp,bid_value, quantity}).

%converts a bid record in a tuple
record_to_tuple(bid, Record) ->
    {Record#bid.id_bid,
    Record#bid.id_auction,
    Record#bid.id_user,
    Record#bid.timestamp,
    Record#bid.bid_value,
    Record#bid.quantity};
%converts a bid tuple in a record
record_to_tuple(auction, Record)->
    {Record#auction.id_auction,
    Record#auction.id_agent,
    Record#auction.name,
    Record#auction.image,
    Record#auction.description,
    Record#auction.end_date,
    Record#auction.min_price,
    Record#auction.min_raise,
    Record#auction.sale_quantity}.
%converts a auction tuple in a record
tuple_to_record(bid, Tuple) ->
    Record = #bid{id_bid= element(1,Tuple),
                id_auction = element(2,Tuple),
                id_user= element(3,Tuple),
                timestamp= element(4,Tuple),
                bid_value= element(5,Tuple),
                quantity= element(6,Tuple)},
    Record;
%converts an auction tuple in a record
tuple_to_record(auction, Tuple) ->
    Record = #auction{id_auction= element(1,Tuple),
                    id_agent = element(2,Tuple),
                    name= element(3,Tuple),
                    image= element(4,Tuple),
                    description = element(5,Tuple),
                    end_date= element(6,Tuple),
                    min_price= element(7,Tuple),
                    min_raise= element(8,Tuple),
                    sale_quantity= element(9,Tuple)},
    Record.

%--------------------------------------------------------------------------------------------------------------

initialization(Nodes) ->
    mnesia:create_schema(Nodes),
    %io:format("A: ~p\n", [A]),
    mnesia:start(),
    start_nodes(Nodes),
    mnesia:create_table(auction,[{disc_copies, Nodes},{attributes, record_info(fields, auction)}]),
    %io:format("B: ~p\n", [B]),
    mnesia:create_table(bid, [{disc_copies, Nodes},{attributes, record_info(fields, bid)}]).
    %io:format("C: ~p\n", [C]).

start_nodes(Nodes) ->
    mnesia:start(),
    A = [rpc:call(Node, mnesia, start, [])||Node <- Nodes, Node =/= node()],
    io:format("~p\n", [A]).

stop_nodes(Nodes) ->
    mnesia:stop(),
    [rpc:call(Node, mnesia, stop, [])|| Node <- Nodes].

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
                mnesia:delete({auction, AuctionId})
            end,
    mnesia:transaction(Fun).

delete_bid(BidId) ->
    Fun =   fun() ->
                mnesia:delete({bid, BidId})
            end,
    mnesia:transaction(Fun).

%--- GET -----------------------------------------------------------------------------------------------------------------
get_auction(AuctionId) ->
    Fun =   fun() ->
                mnesia:read(auction,AuctionId)
            end,
    {Code ,AuctionRecord} = mnesia:transaction(Fun),
    if 
        (Code == atomic) and (AuctionRecord =/= []) ->  [H|_] = AuctionRecord, 
                                                        {Code, record_to_tuple(auction, H)};
        (Code == atomic) and (AuctionRecord == []) -> {Code, {}};
        true -> {Code, AuctionRecord}
    end.

%returns the list of bids for an auction
get_bid_list(AuctionId) ->
    Fun =   fun() ->
                mnesia:match_object(bid, {bid, '_', AuctionId, '_', '_', '_', '_'}, read)
            end,
    {Code, BidRecords} = mnesia:transaction(Fun),
    if 
        (Code == atomic) and (BidRecords =/= []) -> {Code ,[record_to_tuple(bid,BidRecord) || BidRecord <- BidRecords]};
        true -> {Code, BidRecords}
    end.

%returns the list of bids for an auction of a given user
get_bid_list(AuctionId, UserId) ->
    Fun =   fun() ->
                mnesia:match_object(bid, {bid, '_', AuctionId, UserId, '_', '_', '_'}, read)
            end,
    {Code , BidRecords} = mnesia:transaction(Fun),
    if 
        (Code == atomic) and (BidRecords =/= []) -> {Code, [record_to_tuple(bid,BidRecord) || BidRecord <- BidRecords]};
        true -> {Code, BidRecords}
    end.


get_auction_list(Page) ->
    Fun =   fun() ->
                {MegaSecs, Secs, _} = os:timestamp(),
                UnixTime = MegaSecs * 1000000 + Secs,
                MatchHead = #auction{id_auction='$1', 
                                    id_agent='$2', 
                                    name='$3', 
                                    image='$4', 
                                    description='$5', 
                                    end_date='$6', 
                                    min_price='$7', 
                                    min_raise='$8', 
                                    sale_quantity='$9'},
                Guard = [{'>','$6', UnixTime}],
                Result = ['$_'],
                Match = mnesia:select(auction,[{MatchHead, Guard, Result}],Page*?PAGE_SIZE, read),
                if 
                    (Match =/= '$end_of_table') and (Page == 1) -> {List, _} = Match, lists:reverse(List);
                    Match =/= '$end_of_table' -> {List, _} = Match, lists:sublist(lists:reverse(List),(Page-1)*?PAGE_SIZE + 1, length(List));
                    true -> []
                end
            end,
    {atomic, AuctionList} = mnesia:transaction(Fun),
    {atomic, [record_to_tuple(auction, Auction)|| Auction <- AuctionList]}.


%returns the list of auctions in which the user has made at least 1 bid
get_bidder_auctions(IdBidder)->
    Fun =   fun() ->
                MatchHead = #bid{id_auction='$1', id_user= IdBidder, _='_'},
                Guard = [],
                Result = ['$1'],
                Auctions = mnesia:select(bid,[{MatchHead, Guard, Result}]),
                List = [record_to_tuple(auction, lists:nth(1,mnesia:read(auction,Auction)))|| Auction <- Auctions],
                lists:usort(List)
            end,
    mnesia:transaction(Fun).

%returns the list of auctions created by an agent
get_agent_auctions(IdAgent) ->
    Fun =   fun() ->
                mnesia:match_object(auction, {auction, '_', IdAgent, '_','_', '_', '_', '_', '_', '_'}, read)
            end,
    {Code, AuctionList} = mnesia:transaction(Fun),
    if 
        (Code == atomic) and (AuctionList =/= []) -> {Code, [record_to_tuple(auction,Auction) || Auction <- AuctionList]};
        true -> {Code, AuctionList}
    end.


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



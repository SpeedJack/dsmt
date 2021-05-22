package it.unipi.dsmt.das.ws.endpoints;

import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.decode.AuctionStateDecoder;
import it.unipi.dsmt.das.ws.encode.AuctionStateEncoder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


@ServerEndpoint(
        value = "/auction/{auction_id}",
        encoders= {AuctionStateEncoder.class},
        decoders= {AuctionStateDecoder.class})

public class AuctionEndpoint {
    public static final Map<Integer, Queue<Session>> subscribers = new ConcurrentHashMap<>();

    @OnOpen
    public void open(Session session, @PathParam("auction_id") int auctionId){
        subscribers.putIfAbsent(auctionId, new ConcurrentLinkedQueue<>());
        subscribers.get(auctionId).add(session);
    }

/**    @OnMessage
    public void onMessage(Session session, Message message){

        if(message instanceof BidMessage);
        //TODO handle bid message
        //sendToAll(id, subscribers[Id])
        else if(message instanceof SubscriptionMessage);
        //TODO handle subscription message

        //Default do nothing
    }
**/
    //TODO implement methods for message handling

    @OnClose
    public void close(Session session, @PathParam("auction_id") int auctionId){
        Queue<Session> subs = subscribers.get(auctionId);
        if (subs == null)
            return;
        subs.remove(session);
    }

    @OnError
    public void handleError(){};

    public static void closeAuction(int id){
        subscribers.get(id).forEach(session -> {
                session.getAsyncRemote().sendText("END");
        });
    };

    public static void updateAuction(int id, AuctionState state){
        subscribers.get(id).forEach( session -> {
                session.getAsyncRemote().sendObject(state);
        });
    };
}

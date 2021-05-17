package it.unipi.dsmt.das.endpoints;

import it.unipi.dsmt.das.ejbs.interfaces.AuctionManager;
import it.unipi.dsmt.das.endpoints.messages.BidMessage;
import it.unipi.dsmt.das.endpoints.messages.Message;
import it.unipi.dsmt.das.endpoints.messages.SubscriptionMessage;
import it.unipi.dsmt.das.endpoints.messages.conversion.BidMessageEncoder;
import it.unipi.dsmt.das.endpoints.messages.conversion.Decoder;
import it.unipi.dsmt.das.endpoints.messages.conversion.SubscriptionMessageEncoder;
import it.unipi.dsmt.das.endpoints.messages.conversion.UpdateMessageEncoder;
import it.unipi.dsmt.das.model.AuctionState;

import javax.ejb.EJB;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


@ServerEndpoint(
        value = "/auction/{auction_id}",
        encoders= {SubscriptionMessageEncoder.class, BidMessageEncoder.class, UpdateMessageEncoder.class},
        decoders= {Decoder.class})

public class AuctionEndpoint {
    public static Map<Integer, Queue<Session>> subscribers = new ConcurrentHashMap<>();
    public static Map<Integer, AuctionState> ongoingAuctions = new ConcurrentHashMap<>();
    @EJB
    public static AuctionManager manager;

    @OnOpen
    public void open(Session session, @PathParam("auction_id") int auctionId){
        subscribers.putIfAbsent(auctionId, new ConcurrentLinkedQueue<>());
        subscribers.get(auctionId).add(session);
    }

    @OnMessage
    public void onMessage(Session session, Message message){

        if(message instanceof BidMessage);
        //TODO handle bid message
        else if(message instanceof SubscriptionMessage);
        //TODO handle subscription message

        //Default do nothing
    }

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
        ongoingAuctions.remove(id);
        subscribers.get(id).forEach(session -> {
            try {
                session.getBasicRemote().sendText("END");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    };

    public static void updateAuction(int id, AuctionState state){
        ongoingAuctions.put(id, state);
        subscribers.get(id).forEach( session -> {
            try {
                session.getBasicRemote().sendObject(state);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    };
}

package it.unipi.dsmt.das.ws.endpoints;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.decode.AuctionDecoder;
import it.unipi.dsmt.das.ws.encode.AuctionStateEncoder;
import it.unipi.dsmt.das.ws.encode.AuctionStateMessageEncoder;

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
        encoders= {AuctionStateEncoder.class},
        decoders= {AuctionDecoder.class})

public class AuctionEndpoint {
    public static final Map<Long, Queue<Session>> subscribers = new ConcurrentHashMap<>();
    @EJB
    AuctionStatePublisher publisher;

    @OnOpen
    public void open(Session session, @PathParam("auction_id") long auctionId){
        subscribers.putIfAbsent(auctionId, new ConcurrentLinkedQueue<>());
        subscribers.get(auctionId).add(session);
        AuctionState state = publisher.getState(auctionId);
        if (state == null)
            return;
        try {
            session.getBasicRemote().sendObject(state);
        } catch (IOException | EncodeException e) {
            close(session,auctionId);
        }
    }

    @OnClose
    public void close(Session session, @PathParam("auction_id") long auctionId){
        Queue<Session> subs = subscribers.get(auctionId);
        if (subs == null)
            return;
        subs.remove(session);
    }

    @OnError
    public void handleError(Throwable error){
        error.printStackTrace();
    };

    public static void closeAuction(long id){
        subscribers.get(id).forEach(session -> {
                session.getAsyncRemote().sendText("CLOSE");
        });
    };

    public static void updateAuction(long id, AuctionState state){
        System.out.println(state);
        subscribers.get(id).forEach( session -> {
                session.getAsyncRemote().sendObject(state);
        });
    };
}

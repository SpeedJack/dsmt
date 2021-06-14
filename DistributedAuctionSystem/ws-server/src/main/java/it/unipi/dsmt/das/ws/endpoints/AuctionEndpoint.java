package it.unipi.dsmt.das.ws.endpoints;

import com.google.gson.Gson;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.model.Auction;
import it.unipi.dsmt.das.model.AuctionData;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.model.LowestBids;

import javax.ejb.EJB;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


@ServerEndpoint(
        value = "/auction/{auction_id}"
)

public class AuctionEndpoint {
    public static final Map<Long, Queue<Session>> subscribers = new ConcurrentHashMap<>();
    public static final Map<Long, Auction> auctions = new ConcurrentHashMap<>();
    @EJB
    AuctionStatePublisher publisher;
    @EJB
    AuctionManager manager;
    private static final Gson gson = new Gson();

    @OnOpen
    public void open(Session session, @PathParam("auction_id") long auctionId){
        subscribers.putIfAbsent(auctionId, new ConcurrentLinkedQueue<>());
        subscribers.get(auctionId).add(session);
        AuctionState state = publisher.getState(auctionId);
        AuctionData data = manager.selectAuction(auctionId, 0);

        if(data == null || state == null)
            return;

        Auction auction = data.getAuction();
        auctions.putIfAbsent(auctionId, auction);

        LowestBids lowestBids = state.getLowestBids(auction);
        try {
            session.getBasicRemote().sendText(gson.toJson(data));
            session.getBasicRemote().sendText(gson.toJson(state));
            session.getBasicRemote().sendText(gson.toJson(lowestBids));
        } catch (IOException e) {
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
        Queue<Session> subs = subscribers.getOrDefault(id, new ConcurrentLinkedQueue<>());
        subs.forEach(session -> {
                session.getAsyncRemote().sendText("CLOSE");
        });
    };

    public static void updateAuction(long id, AuctionState state){
        Queue<Session> subs = subscribers.getOrDefault(id, new ConcurrentLinkedQueue<>());
        Auction auction = auctions.get(id);
        if(auction == null)
            return;
        LowestBids lowestBids = state.getLowestBids(auction);
        subs.forEach( session -> {
            try{
                session.getBasicRemote().sendText(gson.toJson(lowestBids));
                session.getBasicRemote().sendText(gson.toJson(state));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    };
}

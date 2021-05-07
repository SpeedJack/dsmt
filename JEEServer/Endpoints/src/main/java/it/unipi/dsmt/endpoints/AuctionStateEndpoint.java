package it.unipi.dsmt.endpoints;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/auctionstate")
public class AuctionStateEndpoint {
    @OnMessage
    public void getAuctionState(Session session, String message){

    }
}

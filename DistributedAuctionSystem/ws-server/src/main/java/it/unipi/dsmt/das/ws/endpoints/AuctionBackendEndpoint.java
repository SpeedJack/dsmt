package it.unipi.dsmt.das.ws.endpoints;

import it.unipi.dsmt.das.ws.decode.AuctionDecoder;
import it.unipi.dsmt.das.ws.encode.AuctionStateMessageEncoder;
import it.unipi.dsmt.das.ws.messages.AuctionStateMessage;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
        value = "/auction_backend/{auction_id}",
        encoders= {AuctionStateMessageEncoder.class},
        decoders= {AuctionDecoder.class})

public class AuctionBackendEndpoint {
    long auction;
    @OnOpen
    public void open(Session session, @PathParam("auction_id") long auctionId){
        auction=auctionId;
    }

    @OnMessage
    public void onMessage(Session session, AuctionStateMessage message){
        if(message != null) {
            if(message.getType().equals("STATE")) {
                AuctionEndpoint.updateAuction(auction, message.getState());
            } else if (message.getType().equals("CLOSE")) {
                AuctionEndpoint.closeAuction(message.getAuction());
            }
        }
    }

    @OnClose
    public void close(Session session, @PathParam("auction_id") long auctionId){
    }

    @OnError
    public void handleError(Throwable error){
        error.printStackTrace();
    };
}

package it.unipi.dsmt.das.ws.endpoints;

import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.decode.AuctionStateDecoder;
import it.unipi.dsmt.das.ws.encode.AuctionStateEncoder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(
        value = "/auction_backend/{auction_id}",
        encoders= {AuctionStateEncoder.class},
        decoders= {AuctionStateDecoder.class})

public class AuctionBackendEndpoint {
    long auction;
    @OnOpen
    public void open(Session session, @PathParam("auction_id") long auctionId){
        auction=auctionId;
    }

    @OnMessage
    public void onMessage(Session session, AuctionState state){
        if(state != null)
            AuctionEndpoint.updateAuction(auction, state);
        else
            AuctionEndpoint.closeAuction(auction);
    }

    @OnClose
    public void close(Session session, @PathParam("auction_id") int auctionId){
    }

    @OnError
    public void handleError(Throwable error){
        error.printStackTrace();
    };
}

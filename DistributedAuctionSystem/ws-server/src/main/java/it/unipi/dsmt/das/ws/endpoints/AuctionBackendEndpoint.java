package it.unipi.dsmt.das.ws.endpoints;

import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.decode.AuctionDecoder;
import it.unipi.dsmt.das.ws.encode.AuctionStateMessageEncoder;
import it.unipi.dsmt.das.ws.messages.AuctionStateMessage;
import it.unipi.dsmt.das.ws.messages.AuctionSystemMessage;
import it.unipi.dsmt.das.ws.messages.CloseAuctionMessage;

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
    public void onMessage(Session session, AuctionSystemMessage message){
        if(message != null) {
            if (message.getClass().isAssignableFrom(AuctionStateMessage.class)) {
                AuctionEndpoint.updateAuction(auction, ((AuctionStateMessage) message).getState());
            } else if (message.getClass().isAssignableFrom(CloseAuctionMessage.class)) {
                AuctionEndpoint.closeAuction(((CloseAuctionMessage) message).getAuction());
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

package it.unipi.dsmt.das.ws.messages;

import it.unipi.dsmt.das.model.AuctionState;

import java.io.Serializable;

public class AuctionStateMessage extends AuctionSystemMessage implements Serializable {
    long auction;
    AuctionState state;

    public long getAuction() {
        return auction;
    }

    public void setAuction(long auction) {
        this.auction = auction;
    }

    public AuctionState getState() {
        return state;
    }

    public void setState(AuctionState state) {
        this.state = state;
    }

    public AuctionStateMessage(){
        super("STATE");
    }

}

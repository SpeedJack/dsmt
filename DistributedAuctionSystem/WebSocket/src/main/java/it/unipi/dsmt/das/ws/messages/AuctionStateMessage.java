package it.unipi.dsmt.das.ws.messages;

import it.unipi.dsmt.das.model.AuctionState;

import java.io.Serializable;

public class AuctionStateMessage implements Serializable {
    String type;
    long auction;
    AuctionState state;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
        this.type = "STATE";
    }

}

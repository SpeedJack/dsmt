package it.unipi.dsmt.das.ws.messages;

import java.io.Serializable;

public class CloseAuctionMessage extends AuctionSystemMessage implements Serializable {
    long auction;
    public CloseAuctionMessage(){
        super("CLOSE");
    }

    public long getAuction() {
        return auction;
    }

    public void setAuction(long auction) {
        this.auction = auction;
    }
}

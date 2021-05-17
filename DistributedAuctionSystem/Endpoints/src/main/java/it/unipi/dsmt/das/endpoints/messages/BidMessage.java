package it.unipi.dsmt.das.endpoints.messages;

import it.unipi.dsmt.das.endpoints.messages.enums.MessageType;
import it.unipi.dsmt.das.model.Bid;

public class BidMessage extends Message {
    private Bid bid;

    public BidMessage(){super(MessageType.BidMessage);}
    public BidMessage(Bid bid){
        this();
        this.bid = bid;
    }
    public Bid getBid(){ return this.bid; }
    public void setBid(Bid bid){ this.bid = bid; }
}

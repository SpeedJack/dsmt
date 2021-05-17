package it.unipi.dsmt.endpoints.messages;

import it.unipi.dsmt.endpoints.messages.enums.MessageType;
import it.unipi.dsmt.model.Bid;

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

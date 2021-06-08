package it.unipi.dsmt.das.ws.messages;

public class AuctionSystemMessage {
    String type;

    public AuctionSystemMessage(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AuctionSystemMessage(){

    }
}

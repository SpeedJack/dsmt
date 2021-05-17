package it.unipi.dsmt.endpoints.messages;

import it.unipi.dsmt.endpoints.messages.enums.MessageType;

public class SubscriptionMessage extends Message {
    int user;
    int auction;

    public SubscriptionMessage(int user, int auction, boolean unsubscribe) {
        this(unsubscribe);
        this.user = user;
        this.auction = auction;
    }

    public SubscriptionMessage(boolean unsubscribe) {
        super(unsubscribe ? MessageType.UnsubscribeMessage : MessageType.SubscribeMessage);
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getAuction() {
        return auction;
    }

    public void setAuction(int auction) {
        this.auction = auction;
    }
}

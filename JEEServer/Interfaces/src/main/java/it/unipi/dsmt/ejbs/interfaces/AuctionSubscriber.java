package it.unipi.dsmt.ejbs.interfaces;

import javax.ejb.Remote;

@Remote
public interface AuctionSubscriber {

    public void subscribe(int auction, int user);
    public void unsubscribe(int auction, int user);
}

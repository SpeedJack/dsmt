package it.unipi.dsmt.ejbs.interfaces;

import javax.ejb.Remote;

@Remote
public interface AuctionSubscriber {

    void subscribe(int auction, int user);
    void unsubscribe(int auction, int user);
}

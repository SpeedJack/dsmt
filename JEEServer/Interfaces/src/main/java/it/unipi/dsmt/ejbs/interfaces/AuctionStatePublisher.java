package it.unipi.dsmt.ejbs.interfaces;

import it.unipi.dsmt.model.AuctionState;

import javax.ejb.Remote;

@Remote
public interface AuctionStatePublisher {
    public void publishState(int id, AuctionState state);
}

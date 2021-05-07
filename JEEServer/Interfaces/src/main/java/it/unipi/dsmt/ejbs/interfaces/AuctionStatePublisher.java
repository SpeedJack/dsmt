package it.unipi.dsmt.ejbs.interfaces;

import it.unipi.dsmt.model.AuctionState;

import javax.ejb.Remote;

@Remote
public interface AuctionStatePublisher {
    public AuctionState getState(int id);
    public void publishState(int id, AuctionState state);
}

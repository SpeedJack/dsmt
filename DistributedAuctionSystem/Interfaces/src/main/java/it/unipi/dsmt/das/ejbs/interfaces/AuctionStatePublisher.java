package it.unipi.dsmt.das.ejbs.interfaces;

import it.unipi.dsmt.das.model.AuctionState;

import javax.ejb.Remote;

@Remote
public interface AuctionStatePublisher {
    void publishState(int id, AuctionState state);
}

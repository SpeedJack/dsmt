package it.unipi.dsmt.das.ejbs.beans.interfaces;

import it.unipi.dsmt.das.model.AuctionState;

import javax.ejb.Local;
import javax.ejb.Remote;


@Remote
public interface AuctionStatePublisher {
    void publishState(int id, AuctionState state);
}

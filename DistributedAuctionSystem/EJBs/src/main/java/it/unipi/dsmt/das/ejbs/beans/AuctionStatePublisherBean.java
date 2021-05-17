package it.unipi.dsmt.das.ejbs.beans;

import it.unipi.dsmt.das.ejbs.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.endpoints.AuctionEndpoint;
import it.unipi.dsmt.das.model.AuctionState;

import javax.ejb.Singleton;

@Singleton(name = "AuctionStatePublisherEJB")
public class AuctionStatePublisherBean implements AuctionStatePublisher {

  public AuctionStatePublisherBean() {
  }

  @Override
  public void publishState(int id, AuctionState state) {
    AuctionEndpoint.updateAuction(id, state);
  }



}

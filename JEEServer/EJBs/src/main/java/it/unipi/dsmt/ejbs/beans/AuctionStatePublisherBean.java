package it.unipi.dsmt.ejbs.beans;

import it.unipi.dsmt.ejbs.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.endpoints.AuctionEndpoint;
import it.unipi.dsmt.model.AuctionState;

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

package it.unipi.dsmt.ejbs.beans;

import it.unipi.dsmt.ejbs.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.model.AuctionState;

import javax.ejb.Init;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

@Singleton(name = "AuctionStatePublisherEJB")
public class AuctionStatePublisherBean implements AuctionStatePublisher {
  Map<Integer, AuctionState> states;

  public AuctionStatePublisherBean() {
    this.initialize();
  }

  private void initialize(){
    this.states = new HashMap<>();
  }

  @Override
  public AuctionState getState(int id) {
    return this.states.get(id);
  }

  @Override
  public void publishState(int id, AuctionState state) {
    this.states.put(id, state);

  }



}

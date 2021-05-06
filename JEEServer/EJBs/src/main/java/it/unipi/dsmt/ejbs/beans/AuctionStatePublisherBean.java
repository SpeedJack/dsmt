package it.unipi.dsmt.ejbs.beans;

import it.unipi.dsmt.ejbs.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.model.AuctionState;

import javax.ejb.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Singleton(name = "AuctionStatePublisherEJB")
public class AuctionStatePublisherBean implements AuctionStatePublisher {
  Map<Integer, AuctionState> states;
  Map<Integer, Set<Integer>> subscriptions;

  public AuctionStatePublisherBean() {
    this.states = new HashMap<>();
    this.subscriptions = new HashMap<>();
  }

  @Override
  public AuctionState getState(int id) {
    return this.states.get(id);
  }

  @Override
  public void publishState(int id, AuctionState state) {
    this.states.put(id, state);

  }

  @Override
  public void subscribe(int auction, int user) {
    this.subscriptions.get(auction).add(user);
  }

  @Override
  public void unsubscribe(int auction, int user) {
    this.subscriptions.get(auction).remove(user);
  }

}

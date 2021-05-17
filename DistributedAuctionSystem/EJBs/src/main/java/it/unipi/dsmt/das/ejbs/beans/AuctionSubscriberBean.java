package it.unipi.dsmt.das.ejbs.beans;

import it.unipi.dsmt.das.ejbs.interfaces.AuctionSubscriber;

import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Stateless(name = "AuctionSubscriberEJB")
public class AuctionSubscriberBean implements AuctionSubscriber {
  Map<Integer, Set<Integer>> subscriptions;

  public AuctionSubscriberBean() {
    this.initialize();
  }

  private void initialize(){
    this.subscriptions = new HashMap<>();
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

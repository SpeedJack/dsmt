package it.unipi.dsmt.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AuctionState implements Serializable {
    Set<Bid> winningBids;
    AuctionState(){
        winningBids = new HashSet<Bid>();
    }

    public Set<Bid> getWinningBids() {
        return winningBids;
    }

    public void setWinningBids(Set<Bid> winningBids) {
        this.winningBids = winningBids;
    }

    public void addBid(Bid bid){
        this.winningBids.add(bid);
    }

    public void removeBid(Bid bid){
        this.winningBids.remove(bid);
    }

    public void removeBid(User user){
        this.winningBids.removeIf(bid -> bid.user == user.id);
    }
}

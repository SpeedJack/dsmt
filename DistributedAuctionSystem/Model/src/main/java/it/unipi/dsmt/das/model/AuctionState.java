package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AuctionState implements Serializable, Erlangizable<OtpErlangList> {
    Set<Bid> winningBids;
    public AuctionState() {
        winningBids = new HashSet<>();
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

    public Bid getWinning(User user){
             Iterator<Bid> iter = winningBids.iterator();
             Bid winning = new Bid();
             while(iter.hasNext()){
                 Bid bid = iter.next();
                 if (bid.user == user.id)
                 {
                     winning = bid;
                     break;
                 }
             }
             return winning;
    }

    public OtpErlangList erlangize(){
        ArrayList<OtpErlangObject> tempList = new ArrayList<OtpErlangObject>();
        for (Bid bid: winningBids){
            tempList.add(bid.erlangize());
        }
        OtpErlangObject tempArray[] = new OtpErlangObject[tempList.size()];
        return new OtpErlangList(tempList.toArray(tempArray));
    }

    public double getGain(){
        double gain = 0;
        for(Bid bid : winningBids){
            gain += bid.quantity * bid.value;
        }
        return gain;
    };

    public int getSold(){
        int sold = 0;
        for(Bid bid: winningBids){
            sold += bid.quantity;
        }
        return sold;
    }

    public double getLowestBid(Auction auction){
        //SE SONO FINITI GLI OGGETTI
        double lowestBid = auction.getMinPrice();
        int sold = getSold();
        if (sold == auction.getSaleQuantity()){
            lowestBid = 0;
            Iterator<Bid> iter = winningBids.iterator();
            if(iter.hasNext())
                lowestBid = iter.next().getValue();
            while(iter.hasNext()){
                double value = iter.next().getValue();
                if(lowestBid > value)
                    lowestBid = value;
            }
        }
        lowestBid += auction.getMinRaise();
        return lowestBid;
    }

    public void derlangize(OtpErlangList tempList){
        for(OtpErlangObject element : tempList.elements()){
            Bid bid = new Bid();
            bid.derlangize((OtpErlangTuple) element);
            winningBids.add(bid);
        }

    }
}

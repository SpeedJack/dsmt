package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;
import java.util.*;

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

    public LowestBids getLowestBids(Auction auction){
        Map<Integer, Double> lb = new HashMap<>();
        int available = (int) auction.getSaleQuantity() - getSold();
        if(available != 0)
            lb.put(available, auction.getMinPrice());
        double value = auction.getMinPrice();
        Bid lower = getMinBidHigherThen(value);
        int buyQuantity = available;
        double buyPrice;
        while(lower != null){
             buyQuantity += lower.getQuantity();
             buyPrice = lower.getValue() + auction.getMinRaise();
             lb.put(buyQuantity, buyPrice);
             lower = getMinBidHigherThen(buyPrice);
        }
        return new LowestBids(lb);
    }

    public Bid getMinBidHigherThen(double value){
        Bid lower = null;
        for(Bid bid: winningBids){
            if (bid.value >= value){
                if(lower == null || (bid.getValue() < lower.getValue()))
                    lower = new Bid(bid);
                else if(bid.getValue() == lower.getValue())
                    lower.setQuantity(lower.getQuantity() + bid.getQuantity());
            }
        }
        return lower;
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

    public void derlangize(OtpErlangList tempList){
        for(OtpErlangObject element : tempList.elements()){
            Bid bid = new Bid();
            bid.derlangize((OtpErlangTuple) element);
            winningBids.add(bid);
        }

    }
}

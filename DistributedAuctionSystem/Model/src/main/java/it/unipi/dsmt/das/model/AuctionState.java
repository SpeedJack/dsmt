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


    public OtpErlangList erlangize(){
        ArrayList<OtpErlangObject> tempList = new ArrayList<OtpErlangObject>();
        for (Bid bid: winningBids){
            tempList.add(bid.erlangize());
        }
        OtpErlangObject tempArray[] = new OtpErlangObject[tempList.size()];
        return new OtpErlangList(tempList.toArray(tempArray));
    }

    public void derlangize(OtpErlangList tempList){
        for(OtpErlangObject element : tempList.elements()){
            Bid bid = new Bid();
            bid.derlangize((OtpErlangTuple) element);
            winningBids.add(bid);
        }

    }
}

package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;

public class AuctionData implements Serializable, Erlangizable<OtpErlangTuple> {
    Auction auction;
    BidList bidList;

    public AuctionData() {
    }

    public AuctionData(Auction auction, BidList bidList) {
        this.auction = auction;
        this.bidList = bidList;
    }

    public Auction getAuction() {
        return auction;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public BidList getList() {
        return bidList;
    }

    public void setList(BidList bidList) {
        this.bidList = bidList;
    }


    public OtpErlangTuple erlangize(){
        return new OtpErlangTuple(
                new OtpErlangObject[] {
                        auction.erlangize(),
                        bidList.erlangize()
                });
    }

    public void derlangize(OtpErlangTuple tuple){

        auction.derlangize((OtpErlangTuple)tuple.elementAt(1));
        bidList.derlangize(((OtpErlangList)tuple.elementAt(3)));

    }
}

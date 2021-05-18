package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.OtpErlangTuple;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;

public class Auction implements Serializable, Erlangizable<OtpErlangTuple> {
    AuctionData data;
    BidList list;
    AuctionState state;

    public Auction(AuctionData data, BidList list, AuctionState state) {
        this.data = data;
        this.list = list;
        this.state = state;
    }

    public Auction(AuctionData data, BidList list) {
        this(data, list, null);
    }

    public AuctionData getData() {
        return data;
    }

    public void setData(AuctionData data) {
        this.data = data;
    }

    public BidList getList() {
        return list;
    }

    public void setList(BidList list) {
        this.list = list;
    }

    public AuctionState getState() {
        return state;
    }

    public void setState(AuctionState state) {
        this.state = state;
    }

    public Auction() {
    }
}

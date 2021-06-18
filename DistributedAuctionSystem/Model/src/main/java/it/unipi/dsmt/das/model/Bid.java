package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;

public class Bid implements Serializable, Erlangizable<OtpErlangTuple> {
    long id;
    long auction;
    long user;
    long timestamp;
    double value;
    long quantity;

    public Bid() {
        this.id = -1;
    }

    public Bid(long auction, long user, long timestamp, double value, long quantity) {
        this.auction = auction;
        this.user = user;
        this.timestamp = timestamp;
        this.value = value;
        this.quantity = quantity;
        this.id = this.hashCode();
    }

    public Bid(Bid bid) {
        this.auction = bid.getAuction();
        this.quantity = bid.getQuantity();
        this.user = bid.getUser();
        this.value = bid.getValue();
        this.timestamp = bid.getTimestamp();
        this.id = bid.getId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAuction() { return auction;}

    public void setAuction(long auction) { this.auction = auction;}

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }


    public OtpErlangTuple erlangize(){
        return new OtpErlangTuple(
                new OtpErlangObject[] {
                        new OtpErlangLong(id),
                        new OtpErlangLong(auction),
                        new OtpErlangLong(user),
                        new OtpErlangLong(timestamp),
                        new OtpErlangDouble(value),
                        new OtpErlangLong(quantity)
                });
    }

    public void derlangize(OtpErlangTuple tuple){
        if(tuple.arity() != 6){
            return;
        }
        setId(((OtpErlangLong)tuple.elementAt(0)).longValue());
        setAuction(((OtpErlangLong)tuple.elementAt(1)).longValue());
        setUser(((OtpErlangLong)tuple.elementAt(2)).longValue());
        setTimestamp(((OtpErlangLong)tuple.elementAt(3)).longValue());
        setValue(((OtpErlangDouble)tuple.elementAt(4)).doubleValue());
        setQuantity(((OtpErlangLong)tuple.elementAt(5)).longValue());

    }
}

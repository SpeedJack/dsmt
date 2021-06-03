package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;

public class Bid implements Serializable, Erlangizable<OtpErlangTuple> {
    int id;
    int auction;
    int user;
    long timestamp;
    float value;
    int quantity;

    public Bid() {
    }

    public Bid(int auction, int user, long timestamp, float value, int quantity) {
        this.auction = auction;
        this.user = user;
        this.timestamp = timestamp;
        this.value = value;
        this.quantity = quantity;

        this.id = this.hashCode();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuction() { return auction;}

    public void setAuction(int auction) { this.auction = auction;}

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public OtpErlangTuple erlangize(){
        return new OtpErlangTuple(
                new OtpErlangObject[] {
                        new OtpErlangInt(id),
                        new OtpErlangInt(auction),
                        new OtpErlangInt(user),
                        new OtpErlangLong(timestamp),
                        new OtpErlangFloat(value),
                        new OtpErlangInt(quantity)
                });
    }

    public void derlangize(OtpErlangTuple tuple){
        if(tuple.arity() != 6){
            return;
        }
        try{
            setId(((OtpErlangInt)tuple.elementAt(0)).intValue());
            setAuction(((OtpErlangInt)tuple.elementAt(1)).intValue());
            setUser(((OtpErlangInt)tuple.elementAt(2)).intValue());
            setTimestamp(((OtpErlangLong)tuple.elementAt(3)).longValue());
            setValue(((OtpErlangFloat)tuple.elementAt(4)).floatValue());
            setQuantity(((OtpErlangInt)tuple.elementAt(5)).intValue());

        } catch (OtpErlangRangeException ex) {
            ex.printStackTrace();
        }
    }
}

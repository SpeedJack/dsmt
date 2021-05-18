package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;

public class Bid implements Serializable, Erlangizable<OtpErlangTuple> {
    int id;
    int auction;
    int user;
    String timestamp;
    float value;
    int quantity;

    public Bid() {
    }

    public Bid(int id, int auction, int user, String timestamp, float value, int quantity) {
        this.id = id;
        this.auction = auction;
        this.user = user;
        this.timestamp = timestamp;
        this.value = value;
        this.quantity = quantity;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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
                        new OtpErlangString(timestamp),
                        new OtpErlangFloat(value),
                        new OtpErlangInt(quantity)
                });
    }

    public void derlangize(OtpErlangTuple tuple){
        try{
            setId(((OtpErlangInt)tuple.elementAt(1)).intValue());
            setAuction(((OtpErlangInt)tuple.elementAt(2)).intValue());
            setUser(((OtpErlangInt)tuple.elementAt(3)).intValue());
            setTimestamp(((OtpErlangString)tuple.elementAt(4)).stringValue());
            setValue(((OtpErlangFloat)tuple.elementAt(5)).floatValue());
            setQuantity(((OtpErlangInt)tuple.elementAt(6)).intValue());

        } catch (OtpErlangRangeException ex) {
            ex.printStackTrace();
        }
    }
}

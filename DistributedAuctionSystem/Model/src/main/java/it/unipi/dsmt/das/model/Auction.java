package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;
import java.time.Instant;

public class Auction implements Serializable, Erlangizable<OtpErlangTuple> {
    long id;
    long agent;
    String name;
    String image;
    String description;
    long endDate;
    double minPrice;
    double minRaise;
    long saleQuantity;

    public Auction() { }
    public Auction(long agent, String name, String image, String description, long endDate, double minPrice, double minRaise, long saleQuantity) {
        this.agent = agent;
        this.name = name;
        this.image = image;
        this.description = description;
        this.endDate = endDate;
        this.minPrice = minPrice;
        this.minRaise = minRaise;
        this.saleQuantity = saleQuantity;

        this.id = this.hashCode();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAgent() {
        return agent;
    }

    public void setAgent(long agent) {
        this.agent = agent;
    }

    public boolean isValid(){
        if(this.getEndDate() <= Instant.now().getEpochSecond())
            return false;
        if(this.getMinPrice() <= 0)
            return false;
        if(this.getName() == null || this.getName().isEmpty())
            return false;
        if(this.getMinRaise() <= 0)
            return false;
        if(this.getSaleQuantity() <= 0)
            return false;
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMinRaise() {
        return minRaise;
    }

    public void setMinRaise(double minRaise) {
        this.minRaise = minRaise;
    }

    public long getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(long saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public boolean isValidBid(Bid bid){
        if(bid.getValue() < getMinPrice())
            return false;
        if(bid.getQuantity() > getSaleQuantity() || bid.getQuantity() <= 0)
            return false;
        if(bid.getUser() == getAgent())
            return false;
        return true;
    }
    //aa
    public OtpErlangTuple erlangize(){
        return new OtpErlangTuple(
                new OtpErlangObject[] {
                        new OtpErlangLong(id),
                        new OtpErlangLong(agent),
                        new OtpErlangString(name),
                        new OtpErlangString(image),
                        new OtpErlangString(description),
                        new OtpErlangLong(endDate),
                        new OtpErlangDouble(minPrice),
                        new OtpErlangDouble(minRaise),
                        new OtpErlangLong(saleQuantity)
                });
    }

    public void derlangize(OtpErlangTuple tuple){
        if(tuple.arity() != 9){
            return;
        }
        setId(((OtpErlangLong)tuple.elementAt(0)).longValue());
        setAgent(((OtpErlangLong)tuple.elementAt(1)).longValue());
        setName(((OtpErlangString)tuple.elementAt(2)).stringValue());
        try {
            setImage(((OtpErlangList)tuple.elementAt(3)).stringValue());
        } catch (OtpErlangException | ClassCastException e) {
            setImage(((OtpErlangString)tuple.elementAt(3)).stringValue());
        }
        setDescription(((OtpErlangString)tuple.elementAt(4)).stringValue());
        setEndDate(((OtpErlangLong)tuple.elementAt(5)).longValue());
        setMinPrice(((OtpErlangDouble)tuple.elementAt(6)).doubleValue());
        setMinRaise(((OtpErlangDouble)tuple.elementAt(7)).doubleValue());
        setSaleQuantity(((OtpErlangLong)tuple.elementAt(8)).longValue());

    }
}

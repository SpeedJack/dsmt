package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;

public class Auction implements Serializable, Erlangizable<OtpErlangTuple> {
    int id;
    int agent;
    String name;
    String image;
    String description;
    String endDate;
    float minPrice;
    float minRaise;
    int saleQuantity;

    public Auction() { }
    public Auction(int id, int agent, String name, String image, String description, String endDate, float minPrice, float minRaise, int saleQuantity) {
        this.id = id;
        this.agent = agent;
        this.name = name;
        this.image = image;
        this.description = description;
        this.endDate = endDate;
        this.minPrice = minPrice;
        this.minRaise = minRaise;
        this.saleQuantity = saleQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgent() {
        return agent;
    }

    public void setAgent(int agent) {
        this.agent = agent;
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMinRaise() {
        return minRaise;
    }

    public void setMinRaise(float minRaise) {
        this.minRaise = minRaise;
    }

    public int getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(int saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public OtpErlangTuple erlangize(){
        return new OtpErlangTuple(
                new OtpErlangObject[] {
                        new OtpErlangInt(id),
                        new OtpErlangInt(agent),
                        new OtpErlangString(name),
                        new OtpErlangString(image),
                        new OtpErlangString(description),
                        new OtpErlangString(endDate),
                        new OtpErlangFloat(minPrice),
                        new OtpErlangFloat(minRaise),
                        new OtpErlangInt(saleQuantity)
                });
    }

    public void derlangize(OtpErlangTuple tuple){
        try{
            setId(((OtpErlangInt)tuple.elementAt(1)).intValue());
            setAgent(((OtpErlangInt)tuple.elementAt(2)).intValue());
            setName(((OtpErlangString)tuple.elementAt(3)).stringValue());
            setImage(((OtpErlangString)tuple.elementAt(4)).stringValue());
            setDescription(((OtpErlangString)tuple.elementAt(5)).stringValue());
            setEndDate(((OtpErlangString)tuple.elementAt(6)).stringValue());
            setMinPrice(((OtpErlangFloat)tuple.elementAt(7)).floatValue());
            setMinRaise(((OtpErlangFloat)tuple.elementAt(8)).floatValue());
            setSaleQuantity(((OtpErlangInt)tuple.elementAt(9)).intValue());

        } catch (OtpErlangRangeException ex) {
            ex.printStackTrace();
        }
    }
}

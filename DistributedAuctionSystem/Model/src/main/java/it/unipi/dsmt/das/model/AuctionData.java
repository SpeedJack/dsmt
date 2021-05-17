package it.unipi.dsmt.das.model;

import it.unipi.dsmt.model.behaviour.Erlangizable;

import java.io.Serializable;
import java.time.Instant;

public class AuctionData implements Serializable, Erlangizable {
    int id;
    int agent;
    String name;
    String image;
    Instant endDate;
    float minPrice;
    float minRaise;
    int saleQuantity;

    public AuctionData(int id, int agent, String name, String image, Instant endDate, float minPrice, float minRaise, int saleQuantity) {
        this.id = id;
        this.agent = agent;
        this.name = name;
        this.image = image;
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

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
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
}

package it.unipi.dsmt.model;

import it.unipi.dsmt.model.behaviour.Erlangizable;

import java.io.Serializable;

public class Bid implements Serializable, Erlangizable {
    int id;
    int user;
    long timestamp;
    float value;
    int quantity;

    public Bid() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Bid(int id, int user, long timestamp, float value, int quantity) {
        this.id = id;
        this.user = user;
        this.timestamp = timestamp;
        this.value = value;
        this.quantity = quantity;
    }
}

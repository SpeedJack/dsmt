package it.unipi.dsmt.model;

import java.io.Serializable;
import java.util.List;

public class BidList implements Serializable {
    List<Bid> list;

    public BidList(List<Bid> list) {
        this.list = list;
    }

    public BidList() {
    }

    public List<Bid> getList() {
        return list;
    }

    public void setList(List<Bid> list) {
        this.list = list;
    }
}

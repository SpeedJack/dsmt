package it.unipi.dsmt.das.model;

import java.util.*;

public class LowestBids {
    private Map<Integer, Double> lowestBids = new HashMap<>();
    public LowestBids(){

    }
    public LowestBids(Map<Integer, Double> lb) {
        this.lowestBids = lb;
    }
    public List<Integer> getQuantities(){
        return new ArrayList<Integer>(lowestBids.keySet());
    }

    public Map<Integer, Double> getMap(){
        return lowestBids;
    }
}

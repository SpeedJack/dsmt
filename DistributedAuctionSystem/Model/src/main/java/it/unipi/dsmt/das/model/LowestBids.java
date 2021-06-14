package it.unipi.dsmt.das.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LowestBids {
    private Map<Integer, Double> lowestBids = new HashMap<>();
    public LowestBids(){

    }
    public LowestBids(Map<Integer, Double> lb) {
        this.lowestBids = lb;
    }
    public Set<Integer> getQuantities(){
        return lowestBids.keySet();
    }

    public Map<Integer, Double> getMap(){
        return lowestBids;
    }
}

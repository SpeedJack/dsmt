package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BidList implements Serializable, Erlangizable<OtpErlangList> {
    List<Bid> list;

    public BidList(List<Bid> list) {
        this.list = list;
    }

    public BidList() {
        list = new ArrayList<>();
    }

    public List<Bid> getList() {
        return list;
    }

    public void setList(List<Bid> list) {
        this.list = list;
    }


    public OtpErlangList erlangize(){
        ArrayList<OtpErlangObject> tempList = new ArrayList<OtpErlangObject>();
        for (Bid bid: list){
            tempList.add(bid.erlangize());
        }
        OtpErlangObject tempArray[] = new OtpErlangObject[tempList.size()];
        return new OtpErlangList(tempList.toArray(tempArray));
    }

    public void derlangize(OtpErlangList tempList){
        if(tempList.arity() == 0){
            return;
        }
        for(OtpErlangObject element : tempList.elements()){
            Bid bid = new Bid();
            bid.derlangize((OtpErlangTuple) element);
            list.add(bid);
        }

    }
}

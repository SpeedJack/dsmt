package it.unipi.dsmt.das.model;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.model.behaviour.Erlangizable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AuctionList implements Serializable, Erlangizable<OtpErlangList> {
    List<Auction> list;

    public AuctionList(List<Auction> list) {
        this.list = list;
    }

    public AuctionList() {
    }

    public List<Auction> getList() {
        return list;
    }

    public void setList(List<Auction> list) {
        this.list = list;
    }


    public OtpErlangList erlangize(){
        ArrayList<OtpErlangObject> tempList = new ArrayList<OtpErlangObject>();
        for (Auction auction: list){
            tempList.add(auction.erlangize());
        }
        OtpErlangObject tempArray[] = new OtpErlangObject[tempList.size()];
        return new OtpErlangList(tempList.toArray(tempArray));
    }

    public void derlangize(OtpErlangList tempList){
        for(OtpErlangObject element : tempList.elements()){
            Auction auction = new Auction();
            auction.derlangize((OtpErlangTuple) element);
            list.add(auction);
        }

    }
}
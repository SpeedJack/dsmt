package it.unipi.dsmt.ejbs.beans;

import it.unipi.dsmt.ejbs.interfaces.AuctionManager;
import it.unipi.dsmt.ejbs.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.model.AuctionState;
import it.unipi.dsmt.model.Bid;
import it.unipi.dsmt.model.BidList;

import javax.ejb.EJB;
import javax.ejb.Singleton;

@Singleton(name = "AuctionManagerEJB")
public class AuctionManagerBean implements AuctionManager {
    @EJB
    AuctionStatePublisher publisher;

    public AuctionManagerBean() {
    }

    void publishState(AuctionState state){

    }

    @Override
    public AuctionState makeBid(Bid bid) {
        return null;
    }

    @Override
    public AuctionState removeBid(Bid bid) {
        return null;
    }

    @Override
    public BidList listBids(int auctionId) {
        return null;
    }

    @Override
    public BidList listBids(int auctionId, int agentId) {
        return null;
    }

    @Override
    public BidList listBids(int auctionId, int agentId, int page) {
        return null;
    }

    @Override
    public BidList listBids(int auctionId, int agentId, int page, int perPage) {
        return null;
    }


}

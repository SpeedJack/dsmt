package it.unipi.dsmt.ejbs.interfaces;

import javax.ejb.Remote;
import it.unipi.dsmt.model.AuctionState;
import it.unipi.dsmt.model.Bid;
import it.unipi.dsmt.model.BidList;

@Remote
public interface AuctionManager {
    void makeBid(Bid bid);
    void removeBid(Bid bid);
    BidList listBids(int auctionId);
    BidList listBids(int auctionId, int agentId);
    BidList listBids(int auctionId, int agentId, int page);
    BidList listBids(int auctionId, int agentId, int page, int perPage);
}

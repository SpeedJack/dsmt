package it.unipi.dsmt.das.ejbs.beans.interfaces;

import javax.ejb.Remote;

import it.unipi.dsmt.das.model.*;


@Remote
public interface AuctionManager {


    String createAuction(Auction auction);
    String deleteAuction(long auctionId);
    AuctionData selectAuction(long auctionId, long userId);
    AuctionList auctionsList(int page);
    AuctionList auctionAgentList(long agentId);
    AuctionList auctionBidderList(long bidderId);

    BidStatus makeBid(Bid bid);
    BidStatus deleteBid(long auctionId, long bidId);
}

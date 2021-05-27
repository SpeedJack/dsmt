package it.unipi.dsmt.das.ejbs.beans.interfaces;

import javax.ejb.Local;
import javax.ejb.Remote;

import it.unipi.dsmt.das.model.*;

import java.util.List;


@Remote
public interface AuctionManager {


    String createAuction(Auction auction);
    String deleteAuction(int auctionId);
    AuctionData selectAuction(int auctionId, int userId);
    AuctionList auctionsList(int page);
    AuctionList auctionAgentList(int agentId);
    AuctionList auctionBidderList(int bidderId);

    BidStatus makeBid(Bid bid);
    BidStatus deleteBid(int auctionId, int bidId);
}

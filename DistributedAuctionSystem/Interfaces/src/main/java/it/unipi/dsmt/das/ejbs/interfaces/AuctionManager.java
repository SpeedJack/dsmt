package it.unipi.dsmt.das.ejbs.interfaces;

import javax.ejb.Remote;

import it.unipi.dsmt.das.model.*;

import java.util.List;

@Remote
public interface AuctionManager {


    String createAuction(AuctionData auction);
    String deleteAuction(int auctionId);
    Auction selectAuction(int auctionId, int userId);
    List<AuctionData> auctionsList(int page);
    List<AuctionData> auctionAgentList(int agentId);
    List<AuctionData> auctionBidderList(int bidderId);

    void makeBid(Bid bid);
    void deleteBid(int auctionId, int bidId);
}

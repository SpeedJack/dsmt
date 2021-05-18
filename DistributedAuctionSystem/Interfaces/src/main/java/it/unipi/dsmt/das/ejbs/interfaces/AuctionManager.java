package it.unipi.dsmt.das.ejbs.interfaces;

import javax.ejb.Remote;

import it.unipi.dsmt.das.model.*;

import java.util.List;

@Remote
public interface AuctionManager {


    String createAuction(Auction auction);
    String deleteAuction(int auctionId);
    AuctionData selectAuction(int auctionId, int userId);
    List<Auction> auctionsList(int page);
    List<Auction> auctionAgentList(int agentId);
    List<Auction> auctionBidderList(int bidderId);

    void makeBid(Bid bid);
    void deleteBid(int auctionId, int bidId);
}

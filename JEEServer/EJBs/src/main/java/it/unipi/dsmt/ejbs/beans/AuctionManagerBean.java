package it.unipi.dsmt.ejbs.beans;

import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;
import it.unipi.dsmt.ejbs.interfaces.AuctionManager;
import it.unipi.dsmt.ejbs.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.endpoints.AuctionEndpoint;
import it.unipi.dsmt.model.AuctionState;
import it.unipi.dsmt.model.Bid;
import it.unipi.dsmt.model.BidList;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.io.IOException;

@Singleton(name = "AuctionManagerEJB")
public class AuctionManagerBean implements AuctionManager {
    @EJB
    AuctionStatePublisher publisher;
    OtpNode node;
    OtpMbox mbox;
    private final String dispatcher_registered_name = "d_%d";
    private final String dispatcher = "d_%d@localhost";
    private final String mboxName = "auction_manager_mbox";
    private final String nodeName = "auction_manager@localhost";

    public AuctionManagerBean() throws IOException {
        this.node = new OtpNode(nodeName);
        this.mbox = node.createMbox(mboxName);
    }

    void publishState(int auctionId, AuctionState state){
        AuctionEndpoint.updateAuction(auctionId, state);
    }

    @Override
    public void makeBid(Bid bid) {
        int dispatcherId = 1;
        mbox.send(String.format(dispatcher_registered_name, dispatcherId),
                String.format(dispatcher, dispatcherId),
                bid.erlangize());
        //mbox.receive();
    }

    @Override
    public void removeBid(Bid bid) {

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

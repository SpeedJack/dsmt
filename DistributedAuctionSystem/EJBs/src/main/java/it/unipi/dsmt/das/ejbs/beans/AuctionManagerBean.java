package it.unipi.dsmt.das.ejbs.beans;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.ejbs.interfaces.AuctionManager;
import it.unipi.dsmt.das.ejbs.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.endpoints.AuctionEndpoint;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.model.Bid;
import it.unipi.dsmt.das.model.BidList;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;

@Stateless(name = "AuctionManagerEJB")
public class AuctionManagerBean implements AuctionManager {
    @EJB
    private final String dispatcherRegisteredName = "d_1_disp@localhost";
    private final String dispatcherNodeName = "disp@localhost";
    private final String mboxName = "auction_manager_mbox";
    private final String nodeName = "auction_manager@localhost";

    AuctionStatePublisher publisher;
    OtpNode node;
    OtpMbox mbox;

    public AuctionManagerBean() throws IOException {
        this.node = new OtpNode(nodeName);
        this.mbox = node.createMbox(mboxName);
    }

    void publishState(int auctionId, AuctionState state){
        AuctionEndpoint.updateAuction(auctionId, state);
    }

    @Override
    public void makeBid(Bid bid) {
        OtpErlangAtom cmd = new OtpErlangAtom("make_bid");
        OtpErlangInt id = new OtpErlangInt(bid.getAuction());
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,bid.erlangize()});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, bid.erlangize());
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if (msgResponse.atomValue().equals("ok")){
                OtpErlangList newAuctionState = (OtpErlangList) response.elementAt(2);
                //converti la lista in un ogetto Java e inviala al singleton che gestisce lo stato delle aste
            }
        } catch (OtpErlangExit otpErlangExit) {
            otpErlangExit.printStackTrace();
        } catch (OtpErlangDecodeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeBid(int bidId) {

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

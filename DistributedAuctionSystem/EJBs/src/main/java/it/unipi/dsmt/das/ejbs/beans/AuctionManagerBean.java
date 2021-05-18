package it.unipi.dsmt.das.ejbs.beans;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.ejbs.interfaces.AuctionManager;
import it.unipi.dsmt.das.ejbs.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.endpoints.AuctionEndpoint;
import it.unipi.dsmt.das.model.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.util.List;

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
    public String createAuction(AuctionData auction) {
        OtpErlangAtom cmd = new OtpErlangAtom("create_auction");
        OtpErlangInt id = new OtpErlangInt(auction.getId());
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,auction.erlangize()});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, reqMsg);
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if (msgResponse.atomValue().equals("ok")){
                return "ok";
            }
            else{
                OtpErlangAtom errorMsg = (OtpErlangAtom) response.elementAt(2);
                return errorMsg.atomValue();
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    public String deleteAuction(int auctionId) {
        OtpErlangAtom cmd = new OtpErlangAtom("delete_auction");
        OtpErlangInt id = new OtpErlangInt(auctionId);
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,nan});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, reqMsg);
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if (msgResponse.atomValue().equals("ok")){
                return "ok";
            }
            else{
                OtpErlangAtom errorMsg = (OtpErlangAtom) response.elementAt(2);
                return errorMsg.atomValue();
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    public Auction selectAuction(int auctionId, int userId) {
        OtpErlangAtom cmd = new OtpErlangAtom("select_auction");
        OtpErlangInt id = new OtpErlangInt(auctionId);
        OtpErlangInt uId = new OtpErlangInt(userId);
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,uId});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, reqMsg);
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if (msgResponse.atomValue().equals("ok")){
                //riconverti la tupla {Auction, BidList} in un Auction e ritornali al servlet
                return null;
            }
            else{
                return null;
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            return null;
        }
    }

    @Override
    public List<AuctionData> auctionsList(int page) {
        OtpErlangAtom cmd = new OtpErlangAtom("auctions_list");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangInt p = new OtpErlangInt(page);
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,nan,p});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, reqMsg);
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if (msgResponse.atomValue().equals("ok")){
                //riconverti la lista in java e ritornali al servlet
                return null;
            }
            else{
                return null;
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            return null;
        }
    }

    @Override
    public List<AuctionData> auctionAgentList(int agentId) {
        OtpErlangAtom cmd = new OtpErlangAtom("auctions_agent_list");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangInt a = new OtpErlangInt(agentId);
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,nan,a});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, reqMsg);
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if (msgResponse.atomValue().equals("ok")){
                //riconverti la lista in java e ritornali al servlet
                return null;
            }
            else{
                return null;
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            return null;
        }
    }

    @Override
    public List<AuctionData> auctionBidderList(int bidderId) {
        OtpErlangAtom cmd = new OtpErlangAtom("auctions_list");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangInt b = new OtpErlangInt(bidderId);
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,nan,b});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, reqMsg);
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if(msgResponse.atomValue().equals("ok")){
                //riconverti la lista in java e ritornali al servlet
                return null;
            }
            else{
                return null;
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            return null;
        }
    }

    @Override
    public void makeBid(Bid bid) {
        OtpErlangAtom cmd = new OtpErlangAtom("make_bid");
        OtpErlangInt id = new OtpErlangInt(bid.getAuction());
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,bid.erlangize()});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, reqMsg);
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if (msgResponse.atomValue().equals("ok")){
                OtpErlangList newAuctionState = (OtpErlangList) response.elementAt(2);
                //converti la lista in un ogetto Java e inviala al singleton che gestisce lo stato delle aste
            }
        } catch (OtpErlangExit | OtpErlangDecodeException otpErlangExit) {
            otpErlangExit.printStackTrace();
        }
    }

    @Override
    public void deleteBid(int auctionId, int bidId) {
        OtpErlangAtom cmd = new OtpErlangAtom("delete_bid");
        OtpErlangInt id = new OtpErlangInt(auctionId);
        OtpErlangInt bId = new OtpErlangInt(bidId);
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,bId});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, reqMsg);
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if (msgResponse.atomValue().equals("ok")){
                OtpErlangList newAuctionState = (OtpErlangList) response.elementAt(2);
                //converti la lista in un ogetto Java e inviala al singleton che gestisce lo stato delle aste
            }
        } catch (OtpErlangExit | OtpErlangDecodeException otpErlangExit) {
            otpErlangExit.printStackTrace();
        }
    }




}

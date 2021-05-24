package it.unipi.dsmt.das.ejbs.beans;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.model.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "AuctionManagerEJB")
public class AuctionManagerBean implements AuctionManager {
    private final String dispatcherRegisteredName = "d_1_disp@localhost";
    private final String dispatcherNodeName = "disp@localhost";
    private final String mboxName = "auction_manager_mbox";
    private final String nodeName = "auction_manager@localhost";
    @EJB
    AuctionStatePublisher publisher;
    OtpNode node;
    OtpMbox mbox;

    public AuctionManagerBean() throws IOException {
       // this.node = new OtpNode(nodeName);
       // this.mbox = node.createMbox(mboxName);
    }


    @Override
    public String createAuction(Auction auction) {
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
    public AuctionData selectAuction(int auctionId, int userId) {
        AuctionData data = null;
        OtpErlangAtom cmd = new OtpErlangAtom("select_auction");
        OtpErlangInt id = new OtpErlangInt(auctionId);
        OtpErlangInt uId = new OtpErlangInt(userId);
        OtpErlangTuple reqMsg = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,uId});
        mbox.send(dispatcherRegisteredName, dispatcherNodeName, reqMsg);
        try {
            OtpErlangObject msg = mbox.receive();
            OtpErlangTuple response = (OtpErlangTuple) msg;
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(1);
            if (msgResponse.atomValue().equals("ok")) {
                OtpErlangTuple dataTuple = (OtpErlangTuple) response.elementAt(2);
                data = new AuctionData();
                data.derlangize(dataTuple);
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public AuctionList auctionsList(int page) {
        /*AuctionList list = null;
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
                list = new AuctionList();
                list.derlangize((OtpErlangList) response.elementAt(2));
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            e.printStackTrace();
        }
        return list;*/
        Auction a1 = new Auction(1,1,"Nintendo DS", "style/img/image1.jpg", "Nintendo DS", "2021/05/27 21:00", 30, 1, 5);
        Auction a2 = new Auction(2,1,"Xiaomi Mi 11", "style/img/image2.jpg", "Xiaomi Mi 11", "2021/05/25 10:00", 300, 5, 2);
        Auction a3 = new Auction(3,1,"Nutella", "style/img/image3.jpg", "Che mondo sarebbe senza Nutella", "2021/05/30 21:00", 1, (float) 0.1, 50);
        List<Auction> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        list.add(a3);
        return new AuctionList(list);
    }

    @Override
    public AuctionList auctionAgentList(int agentId) {
        AuctionList list = null;
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
               list = new AuctionList();
               list.derlangize((OtpErlangList) response.elementAt(2));
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public AuctionList auctionBidderList(int bidderId) {
        AuctionList list = null;
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
               list = new AuctionList();
               list.derlangize((OtpErlangList) response.elementAt(2));
            }
        } catch (OtpErlangExit | OtpErlangDecodeException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public AuctionState makeBid(Bid bid) {
        AuctionState state = null;
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
                state = new AuctionState();
                state.derlangize(newAuctionState);
                publisher.publishState(bid.getAuction(), state);
            }
        } catch (OtpErlangExit | OtpErlangDecodeException otpErlangExit) {
            otpErlangExit.printStackTrace();
        }

        return state;

    }

    @Override
    public AuctionState deleteBid(int auctionId, int bidId) {
        AuctionState state = null;
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
                state = new AuctionState();
                state.derlangize(newAuctionState);
            }
        } catch (OtpErlangExit | OtpErlangDecodeException otpErlangExit) {
            otpErlangExit.printStackTrace();
        }
        return state;
    }




}

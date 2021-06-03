package it.unipi.dsmt.das.ejbs.beans;

import com.ericsson.otp.erlang.*;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.model.*;

import javax.annotation.Resource;
import javax.ejb.*;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless(name = "AuctionManagerEJB")
public class AuctionManagerBean implements AuctionManager {
    private final String dispatcherRegisteredName = "d_1_disp1@localhost";
    private final String dispatcherNodeName = "disp1@localhost";
    private final String mboxName = "auction_manager_mbox";
    private final String nodeName = "auction_manager@localhost";
    @EJB
    AuctionStatePublisher publisher;
    @Resource
    private TimerService timerService;

    OtpNode node;
    OtpMbox mbox;


    public void scheduleCloseTask(Date expriation, Auction auction) {
        this.timerService.createTimer(expriation, auction);
    }

    /**public void scheduleIntervalTask(long timeout, long interval) {
        this.timerService.createTimer(timeout,interval, "I'm an interval timer");
    }**/

    @Timeout
    public void close(Timer timer) {
        publisher.closeAuction((long)timer.getInfo());
    }
    public AuctionManagerBean() {
      // this.node = new OtpNode(nodeName);
      // this.mbox = node.createMbox(mboxName);
    }


    @Override
    public String createAuction(Auction auction) {
        OtpErlangAtom cmd = new OtpErlangAtom("create_auction");
        OtpErlangLong id = new OtpErlangLong(auction.getId());
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,auction.erlangize()});
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null){
                return "error: time expired";
            }
            else {
                OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
                if (msgResponse.atomValue().equals("ok")){
                    this.timerService.createTimer(Instant.EPOCH.getEpochSecond(), auction.getId());
                    return "ok";
                }
                else{
                    OtpErlangTuple errorMsg = (OtpErlangTuple) response.elementAt(1);
                    System.err.println(errorMsg.toString());
                    return errorMsg.toString();
                }
            }
        } catch (OtpErlangExit | OtpErlangDecodeException | IOException | OtpAuthException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    public String deleteAuction(int auctionId) {
        OtpErlangAtom cmd = new OtpErlangAtom("delete_auction");
        OtpErlangInt id = new OtpErlangInt(auctionId);
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd, id, nan});
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null) {
                return "error: time expired";
            }
            else {
                OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
                if (msgResponse.atomValue().equals("ok")) {
                    this.timerService.getTimers().forEach(
                            timer -> {
                                if (timer.getInfo() == id)
                                    timer.cancel();
                            });
                    return "ok";
                } else {
                    OtpErlangAtom errorMsg = (OtpErlangAtom) response.elementAt(1);
                    return errorMsg.atomValue();
                }
            }
            } catch(OtpErlangExit | OtpErlangDecodeException | IOException | OtpAuthException e){
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
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,uId});
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null) {
                System.out.println("TIME EXPIRED");
                return null;
            }
            System.out.println(response);
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
            if (msgResponse.atomValue().equals("ok")) {
                OtpErlangTuple dataTuple = (OtpErlangTuple) response.elementAt(1);
                if(((OtpErlangTuple)dataTuple.elementAt(0)).arity() == 0){
                    return null;
                }
                data = new AuctionData();
                data.derlangize(dataTuple);
            }
        } catch (OtpErlangExit | OtpErlangDecodeException | IOException | OtpAuthException e) {
            e.printStackTrace();
        }
        return data;

        /*Bid b1 = new Bid(1, 3, 1, Instant.now().getEpochSecond(), (float)2.0, 3);
        Bid b2 = new Bid(1, 3, 1, Instant.EPOCH.getEpochSecond(), (float)3.0, 3);
        Auction a3 = new Auction(3,1,"Nutella", "style/img/image3.jpg", "Che mondo sarebbe senza Nutella", 1622660400, 1, (float) 0.1, 50);
        List<Bid> list = new ArrayList<>();
        list.add(b1);
        list.add(b2);
        BidList bids = new BidList(list);
        return new AuctionData(a3, bids);*/
    }

    @Override
    public AuctionList auctionsList(int page) {
        AuctionList list = null;
        OtpErlangAtom cmd = new OtpErlangAtom("auction_list");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangInt p = new OtpErlangInt(1);
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd,nan,p});
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null){
                System.out.println("TIME EXPIRED");
                return null;
            }
            else {
                OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
                if (msgResponse.atomValue().equals("ok")) {
                    if(((OtpErlangList) response.elementAt(1)).arity() == 0)
                        return null;
                    list = new AuctionList();
                    list.derlangize((OtpErlangList) response.elementAt(1));
                }
                else{
                    return null;
                }
            }
        } catch (OtpErlangExit | OtpErlangDecodeException | IOException | OtpAuthException e) {
            e.printStackTrace();
        }
        return list;

        /*Auction a1 = new Auction(1,1,"Nintendo DS", "style/img/image1.jpg", "Nintendo DS", 1622228400, 30, 1, 5);
        Auction a2 = new Auction(2,1,"Xiaomi Mi 11", "style/img/image2.jpg", "Xiaomi Mi 11", 1622361600, 300, 5, 2);
        Auction a3 = new Auction(3,1,"Nutella", "style/img/image3.jpg", "Che mondo sarebbe senza Nutella", 1622660400, 1, (float) 0.1, 50);
        List<Auction> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        list.add(a3);
        return new AuctionList(list);*/
    }

    @Override
    public AuctionList auctionAgentList(int agentId) {
        AuctionList list = null;
        OtpErlangAtom cmd = new OtpErlangAtom("auction_agent_list");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangInt a = new OtpErlangInt(agentId);
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd,nan,a});
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null){
                System.out.println("TIME EXPIRED");
                return null;
            }
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
            if (msgResponse.atomValue().equals("ok")){
                if(((OtpErlangList) response.elementAt(1)).arity() == 0)
                    return null;
               list = new AuctionList();
               list.derlangize((OtpErlangList) response.elementAt(1));
            }
        } catch (OtpErlangExit | OtpErlangDecodeException | IOException | OtpAuthException e) {
            e.printStackTrace();
        }
        return list;

        /*Auction a1 = new Auction(1,1,"Nintendo DS", "style/img/image1.jpg", "Nintendo DS", Instant.EPOCH.getEpochSecond(), 30, 1, 5);
        Auction a3 = new Auction(3,1,"Nutella", "style/img/image3.jpg", "Che mondo sarebbe senza Nutella", Instant.EPOCH.getEpochSecond(), 1, (float) 0.1, 50);
        List<Auction> list = new ArrayList<>();
        list.add(a1);
        list.add(a3);
        return new AuctionList(list);*/
    }

    @Override
    public AuctionList auctionBidderList(int bidderId) {
        AuctionList list = null;
        OtpErlangAtom cmd = new OtpErlangAtom("auction_bidder_list");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangInt b = new OtpErlangInt(bidderId);
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd,nan,b});
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null){
                System.out.println("TIME EXPIRED");
                return null;
            }
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
            if(msgResponse.atomValue().equals("ok")){
                if(((OtpErlangList) response.elementAt(1)).arity() == 0)
                    return null;
               list = new AuctionList();
               list.derlangize((OtpErlangList) response.elementAt(1));
            }
        } catch (OtpErlangExit | OtpErlangDecodeException | IOException | OtpAuthException e) {
            e.printStackTrace();
        }
        return list;

        /*Auction a1 = new Auction(1,1,"Nintendo DS", "style/img/image1.jpg", "Nintendo DS", Instant.EPOCH.getEpochSecond(), 30, 1, 5);
        Auction a3 = new Auction(3,1,"Nutella", "style/img/image3.jpg", "Che mondo sarebbe senza Nutella", Instant.EPOCH.getEpochSecond(), 1, (float) 0.1, 50);
        List<Auction> list = new ArrayList<>();
        list.add(a1);
        list.add(a3);
        return new AuctionList(list);*/
    }

   @Override
    public BidStatus makeBid(Bid bid) {
        BidStatus status = BidStatus.ERROR;
        OtpErlangAtom cmd = new OtpErlangAtom("make_bid");
        OtpErlangLong id = new OtpErlangLong(bid.getAuction());
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,bid.erlangize()});
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null){
                System.out.println("TIME EXPIRED");
                return null;
            }
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
            if (msgResponse.atomValue().equals("ok")){
                OtpErlangList newAuctionState = (OtpErlangList) response.elementAt(1);
                AuctionState state = new AuctionState();
                state.derlangize(newAuctionState);
                publisher.publishState(bid.getAuction(), state);
                status = BidStatus.RECEIVED;
            } else if (msgResponse.atomValue().equals("err")){
                status = BidStatus.EXPIRED;
            } else {
                status = BidStatus.ERROR;
            }
        } catch (OtpErlangExit | OtpErlangDecodeException | IOException | OtpAuthException otpErlangExit) {
            otpErlangExit.printStackTrace();
        }

        return status;

    }

    @Override
    public BidStatus deleteBid(int auctionId, int bidId) {
        OtpErlangAtom cmd = new OtpErlangAtom("delete_bid");
        OtpErlangInt id = new OtpErlangInt(auctionId);
        OtpErlangInt bId = new OtpErlangInt(bidId);
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,bId});
        BidStatus status = BidStatus.ERROR;
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null){
                System.out.println("TIME EXPIRED");
                return null;
            }
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
            if (msgResponse.atomValue().equals("ok")){
                OtpErlangList newAuctionState = (OtpErlangList) response.elementAt(1);
                AuctionState state = new AuctionState();
                state.derlangize(newAuctionState);
                status = BidStatus.RECEIVED;
            } else if (msgResponse.atomValue().equals("err")){
                status = BidStatus.EXPIRED;
            } else {
                status = BidStatus.ERROR;
            }
        } catch (OtpErlangExit | OtpErlangDecodeException | IOException | OtpAuthException otpErlangExit) {
            otpErlangExit.printStackTrace();
        }
        return status;
    }

    public OtpErlangTuple sendRequest(OtpErlangTuple payload) throws OtpErlangExit, OtpErlangDecodeException, IOException, OtpAuthException {
        OtpSelf client = new OtpSelf("client");
        OtpPeer peer = new OtpPeer(dispatcherNodeName);
        OtpConnection conn = client.connect(peer);
        conn.sendRPC("gen_server", "call",
                new OtpErlangObject[] {
                        new OtpErlangTuple(new OtpErlangObject[] {
                                new OtpErlangAtom("global"),
                                new OtpErlangString(dispatcherRegisteredName),
                        }),
                        payload,
                        new OtpErlangInt(5000)
                });
        OtpErlangObject resMsg = conn.receiveRPC();
        conn.close();
        if (resMsg == null){
            return null;
        }
        else{
            return (OtpErlangTuple) resMsg;
        }
    }


}

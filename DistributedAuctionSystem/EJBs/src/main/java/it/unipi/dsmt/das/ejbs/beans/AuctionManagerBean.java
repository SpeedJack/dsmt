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

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

@Startup
@Singleton(name = "AuctionManagerEJB")
public class AuctionManagerBean implements AuctionManager {

    private final int numDispatcherNodes = 1;
    private final int numDispatcherPerNode = 3;
    private final int maxConnectionTriesToDispatcher = 3;

    @EJB
    AuctionStatePublisher publisher;
    @Resource
    private TimerService timerService;
    private OtpSelf client;

    public AuctionManagerBean() {
        synchronized (this)
        {
            if(client == null){
                try {
                    client = new OtpSelf("client");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //this.client = new OtpSelf("client" + currentThread().getName());
    }

    public void scheduleCloseTask(long expiration, Auction auction) {
        this.timerService.createTimer(expiration, auction);
    }

    @Timeout
    public void close(Timer timer) {
        Auction auction = (Auction) timer.getInfo();
        System.out.println("CLOSE AUCTION TIMER EXPIRED: auction -> " + auction.getName());
        publisher.closeAuction(auction.getId());
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
                    long expiration = (auction.getEndDate() - Instant.now().getEpochSecond()) * 1000;

                    System.out.println("TIMER EXPIRES IN: " + expiration + " ms");
                    if(expiration > 0){
                        scheduleCloseTask(expiration, auction);
                    }
                    return "ok";
                }
                else{
                    OtpErlangTuple errorMsg = (OtpErlangTuple) response.elementAt(1);
                    System.err.println(errorMsg.toString());
                    return errorMsg.toString();
                }
            }
        } catch (OtpErlangExit | OtpAuthException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    @Override
    public String deleteAuction(long auctionId) {
        OtpErlangAtom cmd = new OtpErlangAtom("delete_auction");
        OtpErlangLong id = new OtpErlangLong(auctionId);
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
                                Auction auction = (Auction) timer.getInfo();
                                if (auction.getId() == id.longValue())
                                    timer.cancel();
                            });
                    return "ok";
                } else {
                    OtpErlangAtom errorMsg = (OtpErlangAtom) response.elementAt(1);
                    return errorMsg.atomValue();
                }
            }
            } catch(OtpErlangExit | OtpAuthException e){
                e.printStackTrace();
                return e.toString();
            }
    }

    public AuctionState getAuctionState(long auctionId){
        OtpErlangAtom cmd = new OtpErlangAtom("get_auction_state");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangLong id = new OtpErlangLong(auctionId);
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd,id, nan});
        AuctionState state = null;
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null) {
                System.out.println("TIME EXPIRED");
                return null;
            }
            System.out.println(response);
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
            if (msgResponse.atomValue().equals("ok")) {
                OtpErlangList dataList = (OtpErlangList) response.elementAt(1);
                if (dataList.arity() == 0)
                    return null;
                /*if(((OtpErlangTuple)dataList.elementAt(0)).arity() == 0){
                    return null;
                }*/
                state = new AuctionState();
                state.derlangize(dataList);
            }
        } catch (OtpErlangExit | OtpAuthException e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    public AuctionData selectAuction(long auctionId, long userId) {
        AuctionData data = null;
        OtpErlangAtom cmd = new OtpErlangAtom("select_auction");
        OtpErlangLong id = new OtpErlangLong(auctionId);
        OtpErlangLong uId = new OtpErlangLong(userId);
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
        } catch (OtpErlangExit | OtpAuthException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public AuctionList auctionsList(int page) {
        AuctionList list = null;
        OtpErlangAtom cmd = new OtpErlangAtom("auction_list");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangInt p = new OtpErlangInt(page);
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
        } catch (OtpErlangExit | OtpAuthException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public AuctionList auctionAgentList(long agentId) {
        AuctionList list = null;
        OtpErlangAtom cmd = new OtpErlangAtom("auction_agent_list");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangLong a = new OtpErlangLong(agentId);
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
        } catch (OtpErlangExit | OtpAuthException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public AuctionList auctionBidderList(long bidderId) {
        AuctionList list = null;
        OtpErlangAtom cmd = new OtpErlangAtom("auction_bidder_list");
        OtpErlangAtom nan = new OtpErlangAtom("_");
        OtpErlangLong b = new OtpErlangLong(bidderId);
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
        } catch (OtpErlangExit | OtpAuthException e) {
            e.printStackTrace();
        }
        return list;
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
                return status;
            }
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
            if (msgResponse.atomValue().equals("ok")){
                OtpErlangList newAuctionState = (OtpErlangList) response.elementAt(1);
                AuctionState state = new AuctionState();
                state.derlangize(newAuctionState);
                publisher.publishState(bid.getAuction(), state);
                status = BidStatus.RECEIVED;
            } else if (msgResponse.atomValue().equals("err")){
                status = BidStatus.ERROR;
            } else {
                status = BidStatus.ERROR;
            }
        } catch (OtpErlangExit | OtpAuthException otpErlangExit) {
            otpErlangExit.printStackTrace();
        }

        return status;

    }

    @Override
    public BidStatus deleteBid(long auctionId, long bidId) {
        OtpErlangAtom cmd = new OtpErlangAtom("delete_bid");
        OtpErlangLong id = new OtpErlangLong(auctionId);
        OtpErlangLong bId = new OtpErlangLong(bidId);
        OtpErlangTuple obj = new OtpErlangTuple(new OtpErlangObject[]{cmd,id,bId});
        BidStatus status = BidStatus.ERROR;
        try {
            OtpErlangTuple response = sendRequest(obj);
            if (response == null){
                System.out.println("TIME EXPIRED");
                return status;
            }
            OtpErlangAtom msgResponse = (OtpErlangAtom) response.elementAt(0);
            if (msgResponse.atomValue().equals("ok")){
                OtpErlangList newAuctionState = (OtpErlangList) response.elementAt(1);
                AuctionState state = new AuctionState();
                state.derlangize(newAuctionState);
                publisher.publishState(auctionId, state);
                status = BidStatus.RECEIVED;
            } else if (msgResponse.atomValue().equals("err")){
                status = BidStatus.ERROR;
            } else {
                status = BidStatus.ERROR;
            }
        } catch (OtpErlangExit | OtpAuthException otpErlangExit) {
            otpErlangExit.printStackTrace();
        }
        return status;
    }

    public OtpErlangTuple sendRequest(OtpErlangTuple payload) throws OtpErlangExit, OtpAuthException {
        int connectionAttempts = 1;
        OtpErlangObject resMsg = null;
        while(connectionAttempts >= 1 && connectionAttempts <= maxConnectionTriesToDispatcher) {
            try {
                List<String> disp = selectDispatcher();
                //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAA" + disp.get(0) + "  " + disp.get(1));
                //OtpSelf client = new OtpSelf("client" + Instant.now().toEpochMilli());
                OtpPeer peer = new OtpPeer(disp.get(0));
                OtpConnection conn = client.connect(peer);
                conn.sendRPC("gen_server", "call",
                        new OtpErlangObject[]{
                                new OtpErlangTuple(new OtpErlangObject[]{
                                        new OtpErlangAtom("global"),
                                        new OtpErlangString(disp.get(1)),
                                }),
                                payload,
                                new OtpErlangInt(5000)
                        });
                resMsg = conn.receiveRPC();
                conn.close();
                connectionAttempts = 0;

            }catch(IOException e){
                e.printStackTrace();
                connectionAttempts++;
                try {
                    sleep(1);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                    Thread.interrupted();
                }
            }
        }
        return (OtpErlangTuple)resMsg;
    }

    public List<String> selectDispatcher(){
        int randomNode = (int) ((Math.random() * (numDispatcherNodes + 1 - 1)) + 1);
        int randomProcess = (int) ((Math.random() * (numDispatcherPerNode + 1 - 1)) + 1);

        String nodeName = "disp" + randomNode + "@localhost";
        String processName = "d_" + randomProcess + "_" + nodeName;

        List<String> res = new ArrayList<>();
        res.add(nodeName);
        res.add(processName);

        return res;
    }


}

package it.unipi.dsmt.das.web;

import com.google.gson.Gson;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.model.*;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@WebServlet("/bid")
public class BidServlet extends HttpServlet {
    @EJB
    AuctionManager manager;
    @EJB
    AuctionStatePublisher publisher;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        switch (action) {
            case "delete":
                doDeleteBid(request, response);
                break;
            case "make":
                doMakeBid(request, response);
                break;
            default:
                response.setStatus(404);
                response.getWriter().println("ERROR");
        }

    }

    protected void doMakeBid(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        User sessionUser = (User) session.getAttribute("user");
        long auction_id = Long.parseLong(request.getParameter("auction_id"));
        long user_id = sessionUser.getId();
        long timestamp = Instant.now().getEpochSecond();
        double value = Double.parseDouble(request.getParameter("value"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        Bid bid = new Bid(auction_id, user_id, timestamp, value, quantity);
        AuctionData data = manager.selectAuction(auction_id, user_id);
        AuctionState state = publisher.getState(auction_id);
        int returnCode = 500;
        if(data != null){
            Auction auction = data.getAuction();
            if(auction != null){
                boolean valid = auction.isValidBid(bid);
                if(state!=null) {
                    LowestBids lb = state.getLowestBids(auction);
                    Integer[] keys = lb.getMap().keySet().toArray(new Integer[lb.getMap().keySet().size()]);
                    Arrays.sort(keys);
                    for(int lbQuantity: keys ){
                        if(lbQuantity <= bid.getQuantity()){
                            if(bid.getValue() < lb.getMap().get(lbQuantity))
                            {
                                valid = false;
                                break;
                            }
                        }
                    }
                    if (valid) {
                        BidStatus status = manager.makeBid(bid);
                        if (status == BidStatus.RECEIVED)
                            returnCode = 200;
                    }
                }
            }
        }
         data = manager.selectAuction(auction_id, user_id);
         response.setStatus(returnCode);
         Gson gson = new Gson();
         response.getWriter().println(gson.toJson(data.getList().getList()));
    }

    private void doDeleteBid(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        long auctionID = Long.parseLong(request.getParameter("auctionID"));
        long bidID = Long.parseLong(request.getParameter("bidID"));
        User user = (User)request.getSession().getAttribute("user");
        int returnCode = 500;
        AuctionData data = manager.selectAuction(auctionID, user.getId());
        if(data != null) {
            BidList list = data.getList();
            if(list != null) {
                List<Bid> bidlist = list.getList();
                if(bidlist != null){
                    boolean found = false;
                    for (Bid bid : bidlist){
                        if(bid.getId() == bidID)
                        {
                            found = true;
                            break;
                        }
                    }
                    if(found){
                        BidStatus status = manager.deleteBid(auctionID, bidID);
                        if(status == BidStatus.RECEIVED)
                            returnCode = 200;
                    }
                }

            }

        }
        data = manager.selectAuction(auctionID, user.getId());
        response.setStatus(returnCode);
        Gson gson = new Gson();
        response.getWriter().println(gson.toJson(data.getList().getList()));
    }

}
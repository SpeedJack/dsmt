package it.unipi.dsmt.das.web;

import com.google.gson.Gson;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/bid")
public class BidServlet extends HttpServlet {
    @EJB
    AuctionManager manager;

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
        BidStatus status = manager.makeBid(bid);
        AuctionData data = manager.selectAuction(auction_id, user_id);
        BidList bidList;
        List<Bid> list = new ArrayList<>();
        if(data!= null) {
            bidList = data.getList();
            if(bidList != null){
                list = bidList.getList();
            }
        }

        request.setAttribute("bids", list);
        request.getRequestDispatcher("bidTable.jsp").forward(request, response);
    }

    private void doDeleteBid(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        long auctionID = Long.parseLong(request.getParameter("auctionID"));
        long bidID = Long.parseLong(request.getParameter("bidID"));
        User user = (User)request.getSession().getAttribute("user");

        BidStatus status = manager.deleteBid(auctionID, bidID);
        AuctionData data = manager.selectAuction(auctionID, user.getId());
        BidList bidList;
        List<Bid> list = new ArrayList<>();
        if(data!= null) {
            bidList = data.getList();
            if(bidList != null){
                list = bidList.getList();
            }
        }
        request.setAttribute("bids", list);
        request.getRequestDispatcher("bidTable.jsp").forward(request, response);
    }

}
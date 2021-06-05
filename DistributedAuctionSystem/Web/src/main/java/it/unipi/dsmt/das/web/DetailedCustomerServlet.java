package it.unipi.dsmt.das.web;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.*;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/detailedCustomer")
public class DetailedCustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private AuctionManager auctionManager;
    public DetailedCustomerServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String message;
        RequestDispatcher dispatcher;
        if (session != null) {
            User sessionUser = (User)session.getAttribute("user");
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("ID", sessionUser.getId());
            long auctionID = Long.parseLong(request.getParameter("auctionID"));
            AuctionData auctionData = auctionManager.selectAuction(auctionID, sessionUser.getId());
            if(auctionData == null)
                dispatcher = request.getRequestDispatcher("error_page.jsp");
            else {
                String date = "Auction closed";
                Auction auction = auctionData.getAuction();
                if(auction == null)
                    dispatcher = request.getRequestDispatcher("error_page.jsp");
                else {
                    if (auction.getAgent() != sessionUser.getId()) {
                        if (auction.getEndDate() > Instant.now().getEpochSecond())
                            date = Long.toString(auction.getEndDate() * 1000);
                        BidList list = auctionData.getList();
                        List<Bid> bids = null;
                        //TODO: recuperare auction state
                        if (list != null) {
                            bids = list.getList();

                            message = "Currently, you are the winner!";
                        } else
                            message = "You haven't offers for this item";
                        request.setAttribute("auction", auction);
                        request.setAttribute("bids", bids);
                        request.setAttribute("message", message);
                        request.setAttribute("date", date);
                        dispatcher = request.getRequestDispatcher("detailedCustomer.jsp");
                    }
                    else{
                        request.setAttribute("auctionID", auctionID);
                        dispatcher = request.getServletContext().getRequestDispatcher("/detailedSeller");
                    }

                }
            }
        }
        else
            dispatcher = request.getRequestDispatcher("index.jsp");

        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.Auction;
import it.unipi.dsmt.das.model.User;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/offers")
public class OffersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private AuctionManager auctionManager;
    public OffersServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String destPage;

        if (session != null) {
            destPage = "offers.jsp";
            User sessionUser = (User)session.getAttribute("user");
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("ID", sessionUser.getId());
            List<Auction> list = auctionManager.auctionBidderList(0).getList();
            if(list == null) {
                list = new ArrayList<>();
                list.add(new Auction( 0, "Empty list", "style/img/auction.jpg", "", Instant.EPOCH.getEpochSecond(), 0, 0, 0));
            }
            request.setAttribute("auctionList", list);
        }
        else
            destPage = "index.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }
}
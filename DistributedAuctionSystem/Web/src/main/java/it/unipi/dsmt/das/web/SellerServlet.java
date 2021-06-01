package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.Auction;
import it.unipi.dsmt.das.model.AuctionList;
import it.unipi.dsmt.das.model.User;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/seller")
public class SellerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private AuctionManager auctionManager;

    public SellerServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String destPage;

        if (session != null) {
            List<Auction> list;
            destPage = "homeSeller.jsp";
            User sessionUser = (User)session.getAttribute("user");
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("ID", sessionUser.getId());
            AuctionList auctionList = auctionManager.auctionAgentList(sessionUser.getId());
            if(auctionList == null) {
                list = new ArrayList<>();
                list.add(new Auction(0, "Empty list", "style/img/auction.jpg", "", Instant.EPOCH.getEpochSecond(), 0, 0, 0));
            }
            else
                list = auctionList.getList();
            request.setAttribute("auctionList", list);
        }
        else
            destPage = "index.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }

}
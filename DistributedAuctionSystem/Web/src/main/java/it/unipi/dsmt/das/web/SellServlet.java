package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.Auction;
import it.unipi.dsmt.das.model.User;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/sell")
public class SellServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final Utility utility = new Utility();
    private AuctionManager auctionManager;
    public SellServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String destPage;
        if (session != null) {
            destPage = "sell.jsp";
            User sessionUser = (User)session.getAttribute("user");
            String username = sessionUser.getUsername();
            request.setAttribute("username", username);
        }
        else
            destPage = "index.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String destPage;

        if (session != null) {
            destPage = "sell.jsp";
            String message;
            User sessionUser = (User)session.getAttribute("user");
            String username = sessionUser.getUsername();
            request.setAttribute("username", username);
            Auction auction = new Auction(  0, 0, request.getParameter("name"),
                                            request.getParameter("userfile"),
                                            request.getParameter("description"),
                                            utility.getTimestamp(request.getParameter("day") + " " +
                                                    request.getParameter("hour")),
                                            Float.parseFloat(request.getParameter("minimum_bid")),
                                            Float.parseFloat(request.getParameter("minimum_raise")),
                                            Integer.parseInt(request.getParameter("object")));
            String res = auctionManager.createAuction(auction);
            if(res.equals("ok"))
                message = "Object correctly inserted";
            else
                message = "Error in inserting new object";
            request.setAttribute("message", message);
        }
        else
            destPage = "index.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }
}
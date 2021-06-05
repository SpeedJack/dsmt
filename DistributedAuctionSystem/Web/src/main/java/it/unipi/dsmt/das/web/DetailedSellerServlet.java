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

@WebServlet("/detailedSeller")
public class DetailedSellerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @EJB
    private AuctionManager auctionManager;
    public DetailedSellerServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String destPage;
        String message;

        if (session != null) {
            destPage = "detailedSeller.jsp";
            User sessionUser = (User)session.getAttribute("user");
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("ID", sessionUser.getId());
            long id = Long.parseLong(request.getParameter("auctionID"));
            AuctionData auctionData = auctionManager.selectAuction(id, sessionUser.getId());
            if(auctionData == null)
                destPage = "error_page.jsp";
            else {
                String date = "Auction closed";
                Auction auction = auctionData.getAuction();
                if(auction != null && auction.getEndDate() > Instant.now().getEpochSecond())
                    date = Long.toString(auction.getEndDate()*1000);
                request.setAttribute("auction", auction);
                request.setAttribute("date", date);
                //TODO: Recuperare auction state
                request.setAttribute("gain", "1000");
                request.setAttribute("sold", "1");
            }
        }
        else
            destPage = "index.jsp";

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
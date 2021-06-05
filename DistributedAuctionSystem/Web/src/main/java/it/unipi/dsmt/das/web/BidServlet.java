package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.Bid;
import it.unipi.dsmt.das.model.BidStatus;
import it.unipi.dsmt.das.model.User;

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

@WebServlet("/bid")
public class BidServlet extends HttpServlet {
    @EJB
    AuctionManager manager;
/*    @Resource(lookup = "jms/bidsQueue")
    Queue queue;
    @Resource(lookup = "jms/bidsQueueCF")
    QueueConnectionFactory connectionFactory;
*/
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        RequestDispatcher dispatcher;
        if (session != null) {
            User sessionUser = (User) session.getAttribute("user");
            request.setAttribute("username", sessionUser.getUsername());
            request.setAttribute("ID", sessionUser.getId());

            long auction_id = Long.parseLong(request.getParameter("auction_id"));
            long user_id = Long.parseLong(request.getParameter("user_id"));
            long timestamp = Instant.now().getEpochSecond();
            double value = Double.parseDouble(request.getParameter("value"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            Bid bid = new Bid(auction_id, user_id, timestamp, value, quantity);
            BidStatus status = manager.makeBid(bid);
            response.getWriter().println(status.toString());

           // request.setAttribute("auctionID", auction_id);
           // dispatcher = request.getServletContext().getRequestDispatcher("/detailedCustomer");
        }
        else {
            dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);
        }
    }

}
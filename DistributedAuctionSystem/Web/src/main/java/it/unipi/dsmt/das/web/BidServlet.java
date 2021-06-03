package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.Bid;
import it.unipi.dsmt.das.model.BidStatus;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

            long auction_id = Long.parseLong(req.getParameter("auction_id"));
            long user_id = Long.parseLong(req.getParameter("user_id"));
            long timestamp = Instant.EPOCH.getEpochSecond();
            double value = Double.parseDouble(req.getParameter("value"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            Bid bid = new Bid(auction_id, user_id, timestamp, value, quantity);
            BidStatus status = manager.makeBid(bid);
            res.getWriter().println(status.toString());

    }

}
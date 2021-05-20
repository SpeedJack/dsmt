package it.unipi.dsmt.das.web;

import it.unipi.dsmt.das.model.Bid;

import javax.annotation.Resource;
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
    @Resource(lookup = "jms/bidsQueue")
    Queue queue;
    @Resource(lookup = "jms/bidsQueueCF")
    QueueConnectionFactory connectionFactory;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String mode = req.getParameter("TYPE") != null ? req.getParameter("TYPE") : "MAKE";


        try (Connection connection = connectionFactory.createQueueConnection()){
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer publisher = session.createProducer(queue);

            connection.start();

            Message message = session.createMessage();
            message.setStringProperty("TYPE", mode);
            Bid bid = new Bid();
            bid.setAuction(Integer.parseInt(req.getParameter("auction")));
            /**TODO Come si calcola l'id?
             * bid.setId(Integer.parseInt(USERID));
             * **/
            bid.setValue(Float.parseFloat(req.getParameter("value")));
            bid.setQuantity(Integer.parseInt(req.getParameter("quantity")));
            bid.setTimestamp(Instant.now().toString());
            bid.setUser(Integer.parseInt(req.getParameter("user")));
            message.setObjectProperty("bid", bid);
            publisher.send(message);

        } catch (JMSException e) {
            res.getWriter().println("Error while trying to send bid " + e.getMessage());
        }

        res.getWriter().println("BID sent");
    }

}
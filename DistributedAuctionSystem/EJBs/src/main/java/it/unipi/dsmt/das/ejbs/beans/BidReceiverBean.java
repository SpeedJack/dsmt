package it.unipi.dsmt.das.ejbs.beans;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.model.Bid;
import it.unipi.dsmt.das.ws.client.WSClient;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@MessageDriven(name = "BidReceiverEJB",
        activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "bidsQueue"),
        @ActivationConfigProperty(
                propertyName = "destinationType",
                propertyValue = "javax.jms.Queue")
})
public class BidReceiverBean implements MessageListener {
    @EJB
    AuctionManager manager;
    @EJB
    AuctionStatePublisher publisher;
    public BidReceiverBean() {

    }

    @Override
    public void onMessage(Message message) {
        try {
            AuctionState state = message.getBody(AuctionState.class);
            int auction = message.getIntProperty("auction");
            boolean close = message.getBooleanProperty("closed");
            if (close)
                handleCloseMessage(auction, state);
            else
                handleStateMessage(auction, state);

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void handleStateMessage(int auction, AuctionState state) {

        WSClient clientEndPoint = new WSClient(auction); //add listener
        clientEndPoint.addMessageHandler(System.out::println);
        // send message to websocket
        clientEndPoint.sendMessage(state);
    }

    public void handleCloseMessage(int auction, AuctionState state) {

        WSClient clientEndPoint = new WSClient(auction); //add listener
        clientEndPoint.addMessageHandler(System.out::println);
        // send message to websocket
        clientEndPoint.sendMessage(state);
    }
}

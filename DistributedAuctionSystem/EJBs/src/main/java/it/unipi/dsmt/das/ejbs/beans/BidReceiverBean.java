package it.unipi.dsmt.das.ejbs.beans;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.client.WSClient;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

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
    AuctionStatePublisher publisher;
    public BidReceiverBean() {

    }

    @Override
    public void onMessage(Message message) {
        try {
            AuctionState state = message.getBody(AuctionState.class);
            long auction = message.getLongProperty("auction");
            boolean close = message.getBooleanProperty("closed");
            if (close)
                handleCloseMessage(auction);
            else
                handleStateMessage(auction, state);

        } catch (JMSException ex) {
            ex.printStackTrace();
        }
    }

    public void handleStateMessage(long auction, AuctionState state) {

        WSClient clientEndPoint = new WSClient(auction);
        clientEndPoint.sendState(state);
    }

    public void handleCloseMessage(long auction) {

        WSClient clientEndPoint = new WSClient(auction);
        clientEndPoint.sendClose();
    }
}

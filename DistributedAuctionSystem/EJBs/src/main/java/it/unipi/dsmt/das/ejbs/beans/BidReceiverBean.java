package it.unipi.dsmt.das.ejbs.beans;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionManager;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.model.Bid;

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
    AuctionManager manager;
    @EJB
    AuctionStatePublisher publisher;
    public BidReceiverBean() {

    }

    @Override
    public void onMessage(Message message) {
        try {
            switch (message.getStringProperty("TYPE")) {
                case "MAKE":
                    handleMakeBidMessage(message);
                    break;
                case "DELETE":
                    handleDeleteBidMessage(message);
                    break;
                default:

                    return;
            }
        } catch (JMSException ex) {
            ex.printStackTrace();
            return;
        }
    }

    public void handleMakeBidMessage(Message message) {
        Bid bid = null;
        try {
            bid = (Bid) message.getObjectProperty("bid");
        } catch (JMSException e) {
            e.printStackTrace();
            return;
        }
        AuctionState state = manager.makeBid(bid);
        publisher.publishState(bid.getAuction(), state);
    }

    public void handleDeleteBidMessage(Message message) {
        Bid bid = null;
        try {
            bid = (Bid) message.getObjectProperty("bid");
        } catch (JMSException e) {
            e.printStackTrace();
            return;
        }
        manager.deleteBid(bid.getAuction(), bid.getId());
    }

}

package it.unipi.dsmt.das.ejbs.beans;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.model.Bid;
import it.unipi.dsmt.das.ws.client.WSClient;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.jms.*;
import java.util.concurrent.ConcurrentHashMap;

@Startup
@Singleton(name = "AuctionStatePublisherEJB")
public class AuctionStatePublisherBean implements AuctionStatePublisher {

  /**
   * Gestione Interesse per le aste
   * Viene chiamato dagli auction manager quando qualcuno fa un offerta / la cancella
   * Chiama la funzione dell'endpoint
   */

  private static final ConcurrentHashMap<Integer, AuctionState> states =
          new ConcurrentHashMap<>();
  @Resource(lookup = "jms/bidsQueueCF")
  private ConnectionFactory connectionFactory;
  @Resource(lookup = "jms/bidsQueue")
  private Queue queue;




  public AuctionStatePublisherBean() { }

  @Override
  public void publishState(int id, AuctionState state) {
    states.put(id, state);
    try {
      broadcast(id, state, false);
    } catch(JMSException ex) {
      ex.printStackTrace();
    }

  }

  public void close(int id){
    try {
      broadcast(id, states.get(id), true);
    } catch (JMSException ex){
      ex.printStackTrace();
    }
  }

  private void broadcast(int id, AuctionState state, boolean close) throws JMSException {
    Connection connection = connectionFactory.createConnection();

    Session session = connection.createSession(true, 0);
    MessageProducer producer = session.createProducer(queue);

    Message message = session.createObjectMessage(state);
    message.setIntProperty("auction", id);
    message.setBooleanProperty("close", close);
    producer.send(message);

    connection.close();
  }
}

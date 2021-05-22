package it.unipi.dsmt.das.ejbs.beans;

import it.unipi.dsmt.das.ejbs.beans.interfaces.AuctionStatePublisher;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.client.WSClient;

import javax.ejb.Singleton;

@Singleton(name = "AuctionStatePublisherEJB")
public class AuctionStatePublisherBean implements AuctionStatePublisher {

  /**
   * Gestione Interesse per le aste
   * Viene chiamato dagli auction manager quando qualcuno fa un offerta / la cancella
   * Chiama la funzione dell'endpoint
   */
  public AuctionStatePublisherBean() {

  }

  @Override
  public void publishState(int id, AuctionState state) {
    WSClient clientEndPoint;
    clientEndPoint = new WSClient(id);
    // add listener
    // clientEndPoint.addMessageHandler(message -> System.out.println(message));
    // send message to websocket
    clientEndPoint.sendMessage(state);
  }



}

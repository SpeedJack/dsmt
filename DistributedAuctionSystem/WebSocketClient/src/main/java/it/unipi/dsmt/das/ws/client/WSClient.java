package it.unipi.dsmt.das.ws.client;

import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.encode.AuctionStateEncoder;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@ClientEndpoint(
        encoders = {AuctionStateEncoder.class})
public class WSClient {

    Session session = null;
    private MessageHandler handler;

    public WSClient(){
        this(0);
    }

    public WSClient(long auction){
        this("ws://localhost:8080/ws/auctions_backend/", auction);
    }

    public WSClient(String endpoint, long auction) {
        try {
            URI endpointURI = new URI(endpoint + String.valueOf(auction));
            System.out.println(endpointURI);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        session.getAsyncRemote().sendText("Opening connection");
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.handler = msgHandler;
    }

    @OnMessage
    public void processMessage(String message) {
        System.out.println("Received message in it.unipi.dsmt.das.ws.client: " + message);
    }

    public void sendState(AuctionState state) {
        this.session.getAsyncRemote().sendObject(state);
    }

    public void sendClose() {
        this.session.getAsyncRemote().sendText("CLOSE");
    }


    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}

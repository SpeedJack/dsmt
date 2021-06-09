package it.unipi.dsmt.das.ws.client;

import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.encode.AuctionStateMessageEncoder;
import it.unipi.dsmt.das.ws.messages.AuctionStateMessage;
import it.unipi.dsmt.das.ws.messages.CloseAuctionMessage;

import java.util.concurrent.Future;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint(
        encoders = {AuctionStateMessageEncoder.class})
public class WSClient {
    long auction;
    Session session = null;
    private MessageHandler handler;

    public WSClient(){
        this(0);
    }

    public WSClient(long auction){
        this("ws://localhost:8080/ws/auction_backend/", auction);
    }

    public WSClient(String endpoint, long auction) {
        try {
            this.auction = auction;
            URI endpointURI = new URI(endpoint + auction);
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
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.handler = msgHandler;
    }

    @OnMessage
    public void processMessage(String message) {
        System.out.println("Received message in it.unipi.dsmt.das.ws.client: " + message);
    }

    public void sendState(AuctionState state) {
        try {
            AuctionStateMessage message = new AuctionStateMessage();
            message.setAuction(this.auction);
            message.setState(state);
            this.session.getBasicRemote().sendObject(message);

        } catch (EncodeException | IOException e) {
            e.printStackTrace();
        }
        this.close();
    }

    public void sendClose(){
        try {
            CloseAuctionMessage message = new CloseAuctionMessage();
            message.setAuction(this.auction);
            this.session.getBasicRemote().sendObject(message);
        } catch (EncodeException | IOException e) {
            e.printStackTrace();
        }
        this.close();
    }

    public void close() {
        if(this.session == null)
            return;
        try{
            this.session.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
        this.session = null;
    }

    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}

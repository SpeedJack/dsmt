package it.unipi.dsmt.das.ws.test;

import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.model.Bid;
import it.unipi.dsmt.das.ws.client.WSClient;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;

public class TestWS {

    public static void main(String[] args) throws DeploymentException, IOException, InterruptedException {
        //WebSocketContainer cc = ContainerProvider.getWebSocketContainer();
        //Session connectToServer = cc.connectToServer(WSClient.class, URI.create("ws://hp:8080/ws/auctions/0/"));
        new CountDownLatch(1).await();
        WSClient client = new WSClient();
        WSClient backend = new WSClient("ws://hp:8080/DAS/auctions/", 0);

        AuctionState state = new AuctionState();

        for (int i = 0; i< 10; ++i){
            Bid bid = new Bid();
            bid.setUser(1);
            bid.setTimestamp(Instant.now().toString());
            bid.setValue(32.0F);
            bid.setQuantity(1);
            bid.setAuction(0);
            state.addBid(bid);
        }

        client.addMessageHandler(System.out::println);
        backend.sendMessage(state);
    }
}

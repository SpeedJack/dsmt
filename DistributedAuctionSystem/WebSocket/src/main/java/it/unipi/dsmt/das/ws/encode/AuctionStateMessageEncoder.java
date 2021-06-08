package it.unipi.dsmt.das.ws.encode;

import com.google.gson.Gson;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.messages.AuctionStateMessage;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class AuctionStateMessageEncoder implements Encoder.Text<AuctionStateMessage> {

    @Override
    public String encode(AuctionStateMessage state) {
        if(state == null)
            return null;
        Gson gson = new Gson();
        return gson.toJson(state);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {
        System.out.println("Destroying encoder...");
    }

}

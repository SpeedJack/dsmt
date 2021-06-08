package it.unipi.dsmt.das.ws.encode;

import com.google.gson.Gson;
import it.unipi.dsmt.das.model.AuctionState;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class CloseAuctionMessageEncoder implements Encoder.Text<AuctionState>{
    @Override
    public String encode(AuctionState object) {
        if(object == null)
            return null;
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}

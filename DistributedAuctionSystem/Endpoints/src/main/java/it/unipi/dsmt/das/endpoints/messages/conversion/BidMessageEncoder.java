package it.unipi.dsmt.das.endpoints.messages.conversion;

import com.google.gson.Gson;
import it.unipi.dsmt.das.endpoints.messages.BidMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class BidMessageEncoder implements Encoder.Text<BidMessage> {
    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(BidMessage bidMessage) throws EncodeException {
        return new Gson().toJson(bidMessage);
    }
}

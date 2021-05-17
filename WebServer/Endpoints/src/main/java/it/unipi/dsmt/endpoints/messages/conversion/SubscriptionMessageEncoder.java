package it.unipi.dsmt.endpoints.messages.conversion;

import com.google.gson.Gson;
import it.unipi.dsmt.endpoints.messages.SubscriptionMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class SubscriptionMessageEncoder implements Encoder.Text<SubscriptionMessage>{

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(SubscriptionMessage subscriptionMessage) throws EncodeException {
       return new Gson().toJson(subscriptionMessage);
    }
}

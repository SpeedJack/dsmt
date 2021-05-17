package it.unipi.dsmt.endpoints.messages.conversion;

import com.google.gson.Gson;
import it.unipi.dsmt.endpoints.messages.UpdateMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class UpdateMessageEncoder implements Encoder.Text<UpdateMessage> {
    @Override
    public String encode(UpdateMessage updateMessage) throws EncodeException {
        return new Gson().toJson(updateMessage);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

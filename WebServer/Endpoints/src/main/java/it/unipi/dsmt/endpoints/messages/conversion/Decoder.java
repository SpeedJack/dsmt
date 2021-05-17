package it.unipi.dsmt.endpoints.messages.conversion;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unipi.dsmt.endpoints.messages.BidMessage;
import it.unipi.dsmt.endpoints.messages.SubscriptionMessage;
import it.unipi.dsmt.endpoints.messages.Message;
import it.unipi.dsmt.endpoints.messages.UpdateMessage;
import it.unipi.dsmt.endpoints.messages.enums.MessageType;

import javax.websocket.DecodeException;
import javax.websocket.EndpointConfig;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Decoder implements javax.websocket.Decoder.Text<Message> {
    private MessageType type;

    @Override
    public Message decode(String json) throws DecodeException {
        Gson gson = new Gson();
        Message message = null;
        if(willDecode(json))
            switch(type) {
                case UpdateMessage:
                    message = gson.fromJson(json, UpdateMessage.class);
                    break;
                case SubscribeMessage:
                case UnsubscribeMessage:
                    message = gson.fromJson(json, SubscriptionMessage.class);
                    break;
                case BidMessage:
                    message = gson.fromJson(json, BidMessage.class);
                    break;
            }
        return message;
    }

    @Override
    public boolean willDecode(String json) {
        if(json!=null && !json.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
            Map<String,Object> message = gson.fromJson(json, type);
            this.type = (MessageType) message.get("type");
            return true;
        } else
            return false;

    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

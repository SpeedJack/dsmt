package it.unipi.dsmt.das.ws.decode;

import com.google.gson.Gson;
import it.unipi.dsmt.das.ws.messages.AuctionStateMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class AuctionDecoder implements Decoder.Text<AuctionStateMessage> {

    @Override
    public AuctionStateMessage decode(String s) {
        Gson gson = new Gson();
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + s);
        return gson.fromJson(s, AuctionStateMessage.class);

    }

    @Override
    public boolean willDecode(String s) {
        return (s != null && ! s.isEmpty());
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

package it.unipi.dsmt.das.ws.decode;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.unipi.dsmt.das.model.AuctionState;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class AuctionStateDecoder implements Decoder.Text<AuctionState> {

    @Override
    public AuctionState decode(String s) throws DecodeException {
        Gson gson = new Gson();
        try {
            return gson.fromJson(s, AuctionState.class);
        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
            throw new DecodeException(s, ex.getMessage(), ex);
        }
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

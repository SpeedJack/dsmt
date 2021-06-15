package it.unipi.dsmt.das.ws.decode;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.unipi.dsmt.das.model.AuctionState;
import it.unipi.dsmt.das.ws.messages.AuctionStateMessage;
import it.unipi.dsmt.das.ws.messages.AuctionSystemMessage;
import it.unipi.dsmt.das.ws.messages.CloseAuctionMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class AuctionDecoder implements Decoder.Text<AuctionSystemMessage> {

    @Override
    public AuctionSystemMessage decode(String s) throws DecodeException {
        Gson gson = new Gson();
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + s);
        try {
            String type = gson.fromJson(s, AuctionSystemMessage.class).getType();
            if(type.equals("STATE"))
                return gson.fromJson(s, AuctionStateMessage.class);
            else if(type.equals("CLOSE"))
                return gson.fromJson(s, CloseAuctionMessage.class);

        } catch (JsonSyntaxException ex) {
            ex.printStackTrace();
            throw new DecodeException(s, ex.getMessage(), ex);
        }
        return new CloseAuctionMessage();
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

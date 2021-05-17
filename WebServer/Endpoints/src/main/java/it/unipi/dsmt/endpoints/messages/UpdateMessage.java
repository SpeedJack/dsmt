package it.unipi.dsmt.endpoints.messages;

import it.unipi.dsmt.endpoints.messages.enums.MessageType;
import it.unipi.dsmt.model.AuctionState;
import it.unipi.dsmt.model.Bid;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class UpdateMessage extends Message {

    Set<Bid> winningBids;

    public UpdateMessage(@NotNull AuctionState auctionState){
        super(MessageType.UpdateMessage);
        if (auctionState.getWinningBids() != null)
            winningBids.addAll(auctionState.getWinningBids());
        else winningBids = new HashSet<>();
    }
}

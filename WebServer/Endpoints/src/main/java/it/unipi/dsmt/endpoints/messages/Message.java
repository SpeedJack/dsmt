package it.unipi.dsmt.endpoints.messages;

import it.unipi.dsmt.endpoints.messages.enums.MessageType;

public abstract class Message {
    MessageType type;

    Message(){};

    Message(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

}

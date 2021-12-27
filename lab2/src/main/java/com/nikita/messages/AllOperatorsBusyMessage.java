package com.nikita.messages;

import com.nikita.entities.Call;

public class AllOperatorsBusyMessage {
    private StartCallMessage startCallMessage;

    public AllOperatorsBusyMessage(StartCallMessage startCallMessage) {
        this.startCallMessage = startCallMessage;
    }

    public StartCallMessage getStartCallMessage() {
        return startCallMessage;
    }
}

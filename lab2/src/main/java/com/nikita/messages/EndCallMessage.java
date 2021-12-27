package com.nikita.messages;

import com.nikita.entities.Call;

public class EndCallMessage {
    private Call call;

    public EndCallMessage(Call call) {
        this.call = call;
    }

    public Call getCall() {
        return call;
    }
}

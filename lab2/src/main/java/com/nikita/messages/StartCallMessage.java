package com.nikita.messages;

import com.nikita.entities.Call;

public class StartCallMessage {
    private Call call;

    public StartCallMessage(Call call) {
        this.call = call;
    }

    public Call getCall() {
        return call;
    }
}

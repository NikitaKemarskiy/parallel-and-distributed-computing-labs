package com.nikita.entities;

import akka.actor.Props;
import com.nikita.entities.Call;

public class Operator {
    private String id;
    private Call lastCall;

    public Operator(String id) {
        this.id = id;
        this.lastCall = null;
    }

    public Call getLastCall() {
        return lastCall;
    }

    public void setLastCall(Call lastCall) {
        this.lastCall = lastCall;
    }
}

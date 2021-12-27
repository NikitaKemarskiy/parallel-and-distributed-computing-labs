package com.nikita.entities;

import akka.actor.ActorRef;
import com.nikita.actors.CallCenter;
import com.nikita.enums.CallStatus;

public class Call {
    private int id;
    private int retries;
    private Client client;
    private Operator operator;
    private CallStatus status;
    private ActorRef callCenterActorRef;

    public Call(int id, Client client) {
        this.id = id;
        this.retries = 0;
        this.client = client;
        this.status = CallStatus.PENDING;
    }

    public int getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public ActorRef getCallCenterActorRef() {
        return callCenterActorRef;
    }

    public void setCallCenterActorRef(ActorRef callCenterActorRef) {
        this.callCenterActorRef = callCenterActorRef;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }

    public int getRetries() {
        return retries;
    }

    public void incrementRetries() {
        retries++;
    }
}

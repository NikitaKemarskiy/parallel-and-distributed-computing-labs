package com.nikita.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.nikita.entities.Operator;
import com.nikita.enums.CallStatus;
import com.nikita.messages.AllOperatorsBusyMessage;
import com.nikita.messages.StartCallMessage;
import com.nikita.messages.EndCallMessage;

import java.util.ArrayList;
import java.util.List;

public class CallCenter extends AbstractActor {
    private final String id;
    private final List<Operator> operators;
    private final LoggingAdapter log;

    public CallCenter(String id, int operatorsNumber) {
        this.id = id;
        this.operators = new ArrayList<>();
        this.log = Logging.getLogger(getContext().getSystem(), this);

        /**
         * Add operators to call center
         */
        for (int i = 0; i < operatorsNumber; i++) {
            operators.add(new Operator(String.format("%s-%d", id, getContext().children().size())));
        }
    }

    public Operator findFreeOperator() {
        return operators
            .stream()
            .filter(
                operator -> operator.getLastCall() == null ||
                operator.getLastCall().getStatus().equals(CallStatus.COMPLETED)
            )
            .findFirst()
            .orElse(null);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(
                StartCallMessage.class,
                startCallMessage -> {
                    Operator operator = findFreeOperator();

                    if (operator == null) {
                        getSender().tell(new AllOperatorsBusyMessage(startCallMessage), getSelf());
                    } else {
                        startCallMessage.getCall().setCallCenterActorRef(getSelf());
                        startCallMessage.getCall().setOperator(operator);
                        operator.setLastCall(startCallMessage.getCall());
                        startCallMessage.getCall().setStatus(CallStatus.IN_PROCESS);
                    }
                })
            .match(
                EndCallMessage.class,
                endCallMessage -> endCallMessage
                    .getCall()
                    .setStatus(CallStatus.COMPLETED))
            .matchAny(o -> log.info("received unknown message"))
            .build();
    }

    public static Props props(String id, int operatorsNumber) {
        return Props.create(CallCenter.class, id, operatorsNumber);
    }
}

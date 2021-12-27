package com.nikita.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.nikita.enums.CallStatus;
import com.nikita.messages.AllOperatorsBusyMessage;
import com.nikita.messages.StartCallMessage;
import com.nikita.messages.EndCallMessage;

import java.util.ArrayList;
import java.util.List;

public class Master extends AbstractActor {
    private final Router router;
    private final LoggingAdapter log;

    private final static int CALL_CENTERS_NUMBER = 3;
    private final static int OPERATORS_NUMBER = 5;
    private final static int MAX_CALL_RETRIES = 5;

    public Master() {
        List<Routee> routees = new ArrayList<>();

        for (int i = 0; i < CALL_CENTERS_NUMBER; i++) {
            ActorRef callCenterActorRef = getContext().actorOf(CallCenter.props(String.valueOf(i), OPERATORS_NUMBER));
            routees.add(new ActorRefRoutee(callCenterActorRef));
        }

        this.router = new Router(new RoundRobinRoutingLogic(), routees);
        this.log = Logging.getLogger(getContext().getSystem(), this);

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(
                StartCallMessage.class,
                startCallMessage -> router.route(startCallMessage, getSelf()))
            .match(
                EndCallMessage.class,
                endCallMessage -> endCallMessage
                    .getCall()
                    .getCallCenterActorRef().tell(endCallMessage, getSelf()))
            .match(
                AllOperatorsBusyMessage.class,
                allOperatorsBusyMessage -> {
                    allOperatorsBusyMessage.getStartCallMessage().getCall().incrementRetries();

                    if (allOperatorsBusyMessage.getStartCallMessage().getCall().getRetries() >= MAX_CALL_RETRIES) {
                        log.error(
                            "failed to start a call. call id: " +
                            allOperatorsBusyMessage.getStartCallMessage().getCall().getId()
                        );
                        allOperatorsBusyMessage.getStartCallMessage().getCall().setStatus(CallStatus.FAILED);
                    } else {
                        log.info(
                            "retry starting a call. call id: " +
                            allOperatorsBusyMessage.getStartCallMessage().getCall().getId()
                        );
                        getSender().tell(allOperatorsBusyMessage.getStartCallMessage(), getSelf());
                    }
                }
            )
            .matchAny(o -> log.info("received unknown message"))
            .build();
    }

    public static Props props() {
        return Props.create(Master.class);
    }
}

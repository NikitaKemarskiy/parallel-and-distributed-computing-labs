package com.nikita;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.nikita.actors.Master;
import com.nikita.entities.Call;
import com.nikita.entities.Client;
import com.nikita.enums.CallStatus;
import com.nikita.messages.EndCallMessage;
import com.nikita.messages.StartCallMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static volatile AtomicInteger callId = new AtomicInteger(1);
    private final static int CLIENTS_NUMBER = 20;

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("library-system");
        ActorRef masterActorRef = actorSystem.actorOf(Master.props());
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < CLIENTS_NUMBER; i++) {
            Thread thread = new Thread(new TestClient(masterActorRef, new Client(String.valueOf(i), String.valueOf(i))));
            thread.start();
            threads.add(thread);
        }

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException err) {
                System.err.println(err);
            }
        });

        actorSystem.terminate();
    }

    private static class TestClient implements Runnable {
        private ActorRef masterActorRef;
        private Client client;

        public TestClient(ActorRef masterActorRef, Client client) {
            this.masterActorRef = masterActorRef;
            this.client = client;
        }

        @Override
        public void run() {
            Call call = new Call(callId.getAndIncrement(), client);
            StartCallMessage startCallMessage = new StartCallMessage(call);
            masterActorRef.tell(startCallMessage, ActorRef.noSender());

            try {
                System.out.println(">>> Start call. Client id: " + client.getId());
                do {
                    Thread.currentThread().sleep(1000);
                } while (startCallMessage.getCall().getStatus() == CallStatus.PENDING);
            } catch (InterruptedException err) {
                System.err.println(err);
            }

            if (startCallMessage.getCall().getStatus() == CallStatus.IN_PROCESS) {
                EndCallMessage endCallMessage = new EndCallMessage(call);
                masterActorRef.tell(endCallMessage, ActorRef.noSender());

                System.out.println("XXX End call. Client id: " + client.getId());
            } else if (startCallMessage.getCall().getStatus() == CallStatus.FAILED) {
                System.out.println("XXX Failed call. Client id: " + client.getId());
            }
        }
    }
}

package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.MessagePublisher;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

public class Broker {

    private static Broker INSTANCE = null;


    private MessageHandlerExecutor msgHandlerExec;
    private SubscriberHandlerExecutor subHandlerExec;


    private Broker() {
        this.msgHandlerExec = new MessageHandlerExecutor();
        this.subHandlerExec = new SubscriberHandlerExecutor();
    }

    private Broker(ExecutorService executor) {
        this.msgHandlerExec = new MessageHandlerExecutor(executor);
        this.subHandlerExec = new SubscriberHandlerExecutor(executor);
    }

    public static Broker getInstance(ExecutorService executor) {
        Objects.requireNonNull(executor);
        if (INSTANCE == null) {
            INSTANCE = new Broker(executor);
        }
        return INSTANCE;
    }

    public static Broker getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Broker();
        }
        return INSTANCE;
    }


    public CompletableFuture<Void> publish(Message message) {
        Objects.requireNonNull(message, "The Message cannot be null");
        return msgHandlerExec.submit(message, subHandlerExec.getSubscribers(message.getTopic()));
    }


    public CompletableFuture<Void> subscribe(Topic topic, MessagePublisher obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The MessagePublisher cannot be null.");

        return subHandlerExec.subscribe(topic, obs);
    }

    public CompletableFuture<Void> updateFilter(Topic topic, String subscriberInBoundPortURI, Predicate<Message> filter) {
        return subHandlerExec.updateFilter(topic, subscriberInBoundPortURI, filter);
    }

    public CompletableFuture<Void> unsubscribe(Topic topic, MessagePublisher obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The MessagePublisher cannot be null.");

        return subHandlerExec.unsubscribe(topic, obs);
    }

    public CompletableFuture<Void> unsubscribe(Topic topic, String subId) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(subId, "The subscriber Id cannot be null.");

        return subHandlerExec.unsubscribe(topic, subId);
    }


    public CompletableFuture<Void> unsubscribe(MessagePublisher obs) {
        Objects.requireNonNull(obs);
        return subHandlerExec.unsubscribe(obs);
    }

    public CompletableFuture<Void> unsubscribe(String subId) {
        Objects.requireNonNull(subId);
        return subHandlerExec.unsubscribe(subId);
    }


    public CompletableFuture<Void> removeTopic(Topic topic) {
        Objects.requireNonNull(topic, "The topic cannot be null.");

        return subHandlerExec.removeTopic(topic);
    }


    protected boolean isSubscribed(MessagePublisher obs) {
        Objects.requireNonNull(obs, "The observer cannot be null.");

        return subHandlerExec.isSubscribed(obs);
    }

    protected boolean isSubscribed(Topic topic, MessagePublisher obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The MessagePublisher cannot be null.");

        return subHandlerExec.isSubscribed(topic, obs);
    }

    protected boolean hasTopic(Topic topic) {
        Objects.requireNonNull(topic);
        return subHandlerExec.hasTopic(topic);
    }

    public void shutdown() {
        msgHandlerExec.shutdown();
        subHandlerExec.clear();
        subHandlerExec.shutdown();
    }

    protected void reboot() {
        subHandlerExec.clear();
    }


}

package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.Observer;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

public class Broker {
    private ConcurrentHashMap<Topic, Set<Observer>> subscribers = new ConcurrentHashMap<>();

    private static Broker INSTANCE = null;


    private MessageHandlerExecutor executorMessage;
    private SubscribeHandlerExecutor executorSubscriber;


    private Broker() {
        this.executorMessage = new MessageHandlerExecutor();
        this.executorSubscriber = new SubscribeHandlerExecutor();
    }

    private Broker(ExecutorService executor) {
        this.executorMessage = new MessageHandlerExecutor(executor);
        this.executorSubscriber = new SubscribeHandlerExecutor(executor);
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
        subscribers = executorSubscriber.getSubscribers();

        return executorMessage.submit(message, subscribers.get(message.getTopic()));
    }


    public CompletableFuture<Void> subscribe(Topic topic, Observer obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The Observer cannot be null.");

        return executorSubscriber.subscrible(topic, obs);
    }

    public CompletableFuture<Void> unsubscribe(Topic topic, Observer obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The Observer cannot be null.");

        return executorSubscriber.unsubscrible(topic, obs);
    }


    public CompletableFuture<Void> unsubscribe(Observer obs) {

        return executorSubscriber.unsubscrible(obs);
    }


    public CompletableFuture<Void> removeTopic(Topic topic) {
        Objects.requireNonNull(topic, "The topic cannot be null.");

        return executorSubscriber.removeTopic(topic);
    }

    protected boolean isSubscribed(Observer obs) {
        Objects.requireNonNull(obs, "The observer cannot be null.");

        return executorSubscriber.isSubscribed(obs);
    }

    protected boolean isSubscribed(Topic topic, Observer obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The Observer cannot be null.");

        return executorSubscriber.isSubscribed(topic, obs);
    }

    protected boolean hasTopic(Topic topic) {
        return executorSubscriber.hasTopic(topic);
    }

    public void shutdown() {
        executorMessage.shutdown();
        executorSubscriber.shutdown();
    }

}
package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.Observer;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

public class Broker {

    private static Broker INSTANCE = null;

    private final ConcurrentHashMap<Topic, Set<Observer>> subscribers = new ConcurrentHashMap<>();

    private MessageHandlerExecutor executorMessage;
    private SubscriberHandlerExecutor executorSubscriber;


    private Broker() {
        this.executorMessage = new MessageHandlerExecutor();
        this.executorSubscriber = new SubscriberHandlerExecutor();
    }

    private Broker(ExecutorService executor) {
        this.executorMessage = new MessageHandlerExecutor(executor);
        this.executorSubscriber = new SubscriberHandlerExecutor(executor);
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
        return executorMessage.submit(message, subscribers.get(message.getTopic()));
    }


    public CompletableFuture<Void> subscribe(Topic topic, Observer obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The Observer cannot be null.");

        return executorSubscriber.subscrible(topic, obs, subscribers);
    }

    public CompletableFuture<Void> unsubscribe(Topic topic, Observer obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The Observer cannot be null.");

        return executorSubscriber.unsubscrible(topic, obs, subscribers);
    }


    public CompletableFuture<Void> unsubscribe(Observer obs) {
        subscribers.keySet()
                .parallelStream()
                .forEach(topic -> unsubscribe(topic, obs));

        return executorSubscriber.unsubscrible(obs, subscribers);
    }


    public void removeTopic(Topic topic) {
        Objects.requireNonNull(topic, "The topic cannot be null.");

//        Set<Observer> subs = subscribers.remove(topic);
//        if (subs != null)
//            executor.submit(topic, subs);

        subscribers.remove(topic);

    }

    protected boolean isSubscribed(Observer obs) {
        Objects.requireNonNull(obs, "The observer cannot be null.");

        return subscribers.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .anyMatch(set -> set.contains(obs));
    }

    protected boolean isSubscribed(Topic topic, Observer obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The Observer cannot be null.");


        if (hasTopic(topic))
            return subscribers.get(topic).contains(obs);

        return false;
    }

    protected boolean hasTopic(Topic topic) {
        return subscribers.containsKey(topic);
    }

    public void shutdown() {
        executorMessage.shutdown();
        executorSubscriber.shutdown();
    }

}
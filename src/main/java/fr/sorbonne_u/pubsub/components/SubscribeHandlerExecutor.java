package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.Observer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class SubscribeHandlerExecutor extends HandlerExecutor {
    private final ConcurrentHashMap<Topic, Set<Observer>> subscribers = new ConcurrentHashMap<>();

    protected SubscribeHandlerExecutor() {
        super();

    }

    protected SubscribeHandlerExecutor(ExecutorService executor) {
        super(executor);
    }

    public ConcurrentHashMap<Topic, Set<Observer>> getSubscribers() {
        return subscribers;
    }

    public CompletableFuture<Void> subscrible(Topic topic, Observer obs) {
        return CompletableFuture.runAsync(() -> addObserver(topic, obs, subscribers), super.executor);

    }


    private void addObserver(Topic topic, Observer obs, ConcurrentHashMap<Topic, Set<Observer>> subscribers) {
        if (topic != null && obs != null) {
            subscribers.computeIfAbsent(topic, t -> ConcurrentHashMap.newKeySet())
                    .add(obs);
        }
    }

    public CompletableFuture<Void> unsubscrible(Topic topic, Observer obs) {
        return CompletableFuture.runAsync(() -> deleteObserver(topic, obs, subscribers), super.executor);

    }


    private void deleteObserver(Topic topic, Observer obs, ConcurrentHashMap<Topic, Set<Observer>> subscribers) {
        if (topic != null && obs != null) {
            subscribers.computeIfPresent(topic, (t, subs) -> {
                subs.remove(obs);
                return subs;
            });
        }
    }

    public CompletableFuture<Void> unsubscrible(Observer obs) {
        return CompletableFuture.runAsync(() -> deleteObserver(obs, subscribers), super.executor);

    }


    private void deleteObserver(Observer obs, ConcurrentHashMap<Topic, Set<Observer>> subscribers) {
        subscribers.keySet()
                .parallelStream()
                .forEach(topic -> deleteObserver(topic, obs, subscribers));
    }

    public boolean isSubscribed(Observer obs) {
        return subscribers.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .anyMatch(set -> set.contains(obs));

    }

    protected boolean isSubscribed(Topic topic, Observer obs) {

        if (hasTopic(topic))
            return subscribers.get(topic).contains(obs);

        return false;
    }

    protected boolean hasTopic(Topic topic) {
        return subscribers.containsKey(topic);
    }

    public CompletableFuture<Void> removeTopic(Topic topic) {

        return CompletableFuture.runAsync(() -> subscribers.remove(topic), super.executor);
    }
}

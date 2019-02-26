package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.Observer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class SubscriberHandlerExecutor extends HandlerExecutor {
    private final ConcurrentHashMap<Topic, Set<Observer>> subscribers = new ConcurrentHashMap<>();

    protected SubscriberHandlerExecutor() {
        super();

    }

    protected SubscriberHandlerExecutor(ExecutorService executor) {
        super(executor);
    }

    public Set<Observer> getSubscribers(Topic topic) {
        return subscribers.get(topic);
    }

    public CompletableFuture<Void> subscribe(Topic topic, Observer obs) {
        return CompletableFuture.runAsync(() -> addObserver(topic, obs), super.executor);

    }

    private void addObserver(Topic topic, Observer obs) {
        subscribers.computeIfAbsent(topic, t -> ConcurrentHashMap.newKeySet())
                .add(obs);
    }

    public CompletableFuture<Void> unsubscribe(Topic topic, Observer obs) {
        return CompletableFuture.runAsync(() -> deleteObserver(topic, obs), super.executor);

    }


    private void deleteObserver(Topic topic, Observer obs) {
        subscribers.computeIfPresent(topic, (t, subs) -> {
            subs.remove(obs);
            return subs;
        });
    }

    public CompletableFuture<Void> unsubscribe(Observer obs) {
        return CompletableFuture.runAsync(() -> deleteObserver(obs), super.executor);

    }


    private void deleteObserver(Observer obs) {
        subscribers.keySet()
                .parallelStream()
                .forEach(topic -> deleteObserver(topic, obs));
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

    public void clear() {
        subscribers.clear();
    }
}

package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.MessagePublisher;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class SubscriberHandlerExecutor extends HandlerExecutor {
    private final ConcurrentHashMap<Topic, Set<MessagePublisher>> subscribers = new ConcurrentHashMap<>();

    protected SubscriberHandlerExecutor() {
        super();
    }

    protected SubscriberHandlerExecutor(ExecutorService executor) {
        super(executor);
    }

    public Set<MessagePublisher> getSubscribers(Topic topic) {
        return subscribers.get(topic);
    }

    public CompletableFuture<Void> subscribe(Topic topic, MessagePublisher obs) {
        return CompletableFuture.runAsync(() -> addObserver(topic, obs), super.executor);
    }

    private void addObserver(Topic topic, MessagePublisher obs) {
        subscribers.computeIfAbsent(topic, t -> ConcurrentHashMap.newKeySet())
                .add(obs);
    }

    public CompletableFuture<Void> unsubscribe(Topic topic, MessagePublisher obs) {
        return CompletableFuture.runAsync(() -> deleteObserver(topic, obs), super.executor);

    }

    private void deleteObserver(Topic topic, MessagePublisher obs) {
        subscribers.computeIfPresent(topic, (t, subs) -> {
            subs.remove(obs);
            return subs;
        });
    }

    public CompletableFuture<Void> unsubscribe(MessagePublisher obs) {
        return CompletableFuture.runAsync(() -> deleteObserver(obs), super.executor);
    }

    private void deleteObserver(MessagePublisher obs) {
        subscribers.keySet()
                .parallelStream()
                .forEach(topic -> deleteObserver(topic, obs));
    }

    public boolean isSubscribed(MessagePublisher obs) {
        return subscribers.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .anyMatch(set -> set.contains(obs));

    }

    protected boolean isSubscribed(Topic topic, MessagePublisher obs) {
        Set<MessagePublisher> observers = getSubscribers(topic);
        return observers != null && observers.contains(obs);
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

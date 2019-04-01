package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.MessagePublisher;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

public class SubscriberHandlerExecutor extends HandlerExecutor {
    private final ConcurrentHashMap<Topic, ConcurrentHashMap<String, MessagePublisher>> subscribers = new ConcurrentHashMap<>();

    protected SubscriberHandlerExecutor() {
        super();
    }

    protected SubscriberHandlerExecutor(ExecutorService executor) {
        super(executor);
    }

    public Collection<MessagePublisher> getSubscribers(Topic topic) {
        Map<String, MessagePublisher> subs = subscribers.get(topic);
        return subs == null ? null : subs.values();
    }

    public CompletableFuture<Void> subscribe(Topic topic, MessagePublisher obs) {
        return CompletableFuture.runAsync(() -> addObserver(topic, obs), super.executor);
    }

    private void addObserver(Topic topic, MessagePublisher obs) {
        subscribers.computeIfAbsent(topic, t -> new ConcurrentHashMap<>())
                .put(obs.getKey(), obs);
    }

    public CompletableFuture<Void> unsubscribe(Topic topic, MessagePublisher obs) {
        return CompletableFuture.runAsync(() -> deleteObserver(topic, obs.getKey()), super.executor);
    }

    public CompletableFuture<Void> unsubscribe(Topic topic, String subId) {
        return CompletableFuture.runAsync(() -> deleteObserver(topic, subId), super.executor);
    }

    private void deleteObserver(Topic topic, String subId) {
        subscribers.computeIfPresent(topic, (t, subs) -> {
            subs.remove(subId);
            return subs;
        });
    }

    public CompletableFuture<Void> unsubscribe(MessagePublisher obs) {
        return CompletableFuture.runAsync(() -> deleteObserver(obs.getKey()), super.executor);
    }

    public CompletableFuture<Void> unsubscribe(String subId) {
        return CompletableFuture.runAsync(() -> deleteObserver(subId), super.executor);
    }

    private void deleteObserver(String subId) {
        subscribers.keySet()
                .parallelStream()
                .forEach(topic -> deleteObserver(topic, subId));
    }

    public CompletableFuture<Void> updateFilter(Topic topic, String subId, Predicate<Message> filter) {
        return CompletableFuture.runAsync(() -> setFilter(topic, subId, filter), super.executor);
    }

    public void setFilter(Topic topic, String subId, Predicate<Message> filter) {
        ConcurrentHashMap<String, MessagePublisher> subs = subscribers.get(topic);
        subs.computeIfPresent(subId, (key, obs) -> {
            obs.setFilter(topic, filter);
            return obs;
        });
    }

    protected boolean isSubscribed(MessagePublisher obs) {
        return subscribers.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .anyMatch(set -> set.containsKey(obs.getKey()));

    }

    protected boolean isSubscribed(Topic topic, MessagePublisher obs) {
        ConcurrentHashMap<String, MessagePublisher> observers = subscribers.get(topic);
        return observers != null && observers.containsKey(obs.getKey());
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

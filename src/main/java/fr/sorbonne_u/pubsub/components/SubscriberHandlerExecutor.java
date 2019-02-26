package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.Observer;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class SubscriberHandlerExecutor extends HandlerExecutor {

    protected SubscriberHandlerExecutor() {
        super();

    }

    protected SubscriberHandlerExecutor(ExecutorService executor) {
        super(executor);
    }


    public CompletableFuture<Void> subscrible(Topic topic, Observer obs, ConcurrentHashMap<Topic, Set<Observer>> subscribers) {
        return CompletableFuture.runAsync(() -> addObserver(topic, obs, subscribers), super.executor);

    }


    private void addObserver(Topic topic, Observer obs, ConcurrentHashMap<Topic, Set<Observer>> subscribers) {
        if (topic != null && obs != null) {
            subscribers.computeIfAbsent(topic, t -> ConcurrentHashMap.newKeySet())
                    .add(obs);
        }
    }

    public CompletableFuture<Void> unsubscrible(Topic topic, Observer obs, ConcurrentHashMap<Topic, Set<Observer>> subscribers) {
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

    public CompletableFuture<Void> unsubscrible(Observer obs, ConcurrentHashMap<Topic, Set<Observer>> subscribers) {
        return CompletableFuture.runAsync(() -> deleteObserver(obs, subscribers), super.executor);

    }


    private void deleteObserver(Observer obs, ConcurrentHashMap<Topic, Set<Observer>> subscribers) {
        subscribers.keySet()
                .parallelStream()
                .forEach(topic -> deleteObserver(topic, obs, subscribers));
    }
}

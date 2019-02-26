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
    private MessageHandlerExecutor executor;

    // TODO SubscriberHandlerExecutor

    private Broker() {
        this.executor = new MessageHandlerExecutor();
    }

    private Broker(ExecutorService executor) {
        this.executor = new MessageHandlerExecutor(executor);
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
        return executor.submit(message, subscribers.get(message.getTopic()));
    }


    public void subscribe(Topic topic, Observer obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The Observer cannot be null.");


        subscribers.computeIfAbsent(topic, t -> ConcurrentHashMap.newKeySet())
                .add(obs);
    }

    public void unsubscribe(Topic topic, Observer obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The Observer cannot be null.");

        subscribers.computeIfPresent(topic, (t, subs) -> {
            subs.remove(obs);
            return subs;
        });

    }


    public void unsubscribe(Observer obs) {
        subscribers.keySet()
                .parallelStream()
                .forEach(topic -> unsubscribe(topic, obs));
    }


    public void removeTopic(Topic topic) {
        Objects.requireNonNull(topic, "The topic cannot be null.");

//        Set<Observer> subs = subscribers.remove(topic);
//        if (subs != null)
//            executor.submit(topic, subs);

        subscribers.remove(topic);

    }

    public boolean isSubscribed(Observer obs) {
        Objects.requireNonNull(obs, "The observer cannot be null.");

        return subscribers.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .anyMatch(set -> set.contains(obs));
    }

    public boolean isSubscribed(Topic topic, Observer obs) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(obs, "The Observer cannot be null.");


        if (hasTopic(topic))
            return subscribers.get(topic).contains(obs);

        return false;
    }

    public boolean hasTopic(Topic topic) {
        return subscribers.containsKey(topic);
    }

    public void shutdown() {
        subscribers.clear();
        executor.shutdown();
    }

    protected void reboot() {
        subscribers.clear();
    }


    public static class MessageHandlerExecutor {
        private ExecutorService executor;

        private MessageHandlerExecutor() {
            executor = Executors.newCachedThreadPool();
        }

        private MessageHandlerExecutor(ExecutorService executor) {
            this.executor = executor;
        }

        private CompletableFuture<Void> submit(Message message, Set<Observer> observers) {
            return CompletableFuture.runAsync(() -> sendMessage(message, observers), executor);
        }

        private void sendMessage(Message message, Set<Observer> observers) {
            if (observers != null) {
                observers.forEach(obs -> obs.update(message));
            }
        }

        public void shutdown() {
            try {
                executor.shutdown();
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                executor.shutdownNow();
            }
        }
    }
}
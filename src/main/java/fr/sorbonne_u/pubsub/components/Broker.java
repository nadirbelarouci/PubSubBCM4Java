package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.Observer;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

public class Broker {

    public final static Broker INSTANCE = new Broker();

    private final ConcurrentHashMap<Topic, Set<Observer>> subscribers = new ConcurrentHashMap<>();
    private MessageHandlerExecutor executor = new MessageHandlerExecutor(10);

    private Broker() {
        super();
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

    public void unsubscribe(Topic topic, Observer msgr) {
        Objects.requireNonNull(topic, "The topic cannot be null.");
        Objects.requireNonNull(msgr, "The Observer cannot be null.");

        subscribers.computeIfPresent(topic, (t, subs) -> {
            subs.remove(msgr);
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

        Set<Observer> subs = subscribers.remove(topic);
        if (subs != null)
            executor.submit(topic, subs);

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
        executor.shutdown();
    }


    public static class MessageHandlerExecutor {
        private ConcurrentHashMap<Topic, Set<Message>> messages = new ConcurrentHashMap<>();

        private ExecutorService executor;

        private MessageHandlerExecutor(int nThreads) {
            executor = Executors.newFixedThreadPool(nThreads);

        }

        private CompletableFuture<Void> submit(Message message, Set<Observer> observers) {
            return CompletableFuture.runAsync(() -> addMessage(message), executor)
                    .thenRunAsync(() -> sendMessage(message, observers), executor);
        }

        private void sendMessage(Message message, Set<Observer> observers) {
            if (observers != null) {
                observers.forEach(receiver -> receiver.update(message));
                messages.get(message.getTopic()).remove(message);
            }
        }

        private void sendMessages(Topic topic, Set<Observer> observers) {
            if (observers != null)
                messages.get(topic).forEach(message -> sendMessage(message, observers));
        }


        private void addMessage(Message message) {
            messages.computeIfAbsent(message.getTopic(), t -> ConcurrentHashMap.newKeySet())
                    .add(message);
        }

        private CompletableFuture<Void> submit(Topic topic, Set<Observer> observers) {
            return CompletableFuture.runAsync(() -> sendMessages(topic, observers));

        }

        public void shutdown() {
            try {

                executor.shutdown();
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                executor.shutdownNow();
            }
        }
    }
}
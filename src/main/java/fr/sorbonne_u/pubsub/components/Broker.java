package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class Broker {
    private static Broker brokerInstance = new Broker();
    private Map<String, Set<Observer>> subscribers = new ConcurrentHashMap<>();

    private Broker() {

    }

    public static Broker getInstance() {
        return brokerInstance;
    }

    public boolean remove(String topic) {
//        subscribers.computeIfPresent(topic,(s, observers) -> {
//            final Set<Observer> subs = observers.get(s);
//
//        });

        return subscribers.remove(topic) != null;
    }

    public boolean unsubscribe(Observer observer) {
        check(observer);
        return subscribers.keySet()
                .parallelStream()
                .map(topic -> unsubscribe(topic, observer))
                .anyMatch(deleted -> deleted);
    }

    public boolean unsubscribe(String topic, Observer observer) {
        check(observer);

        if (subscribers.containsKey(topic))
            return subscribers.get(topic).remove(observer);
        else
            return false;
    }

    public void subscribe(String topic, Observer observer) {
        check(observer);

        subscribers.merge(topic, ConcurrentHashMap.newKeySet(), (observers, observers2) -> {
            observers.add(observer);
            return observers;
        });
    }

    public boolean addTopic(String topic) {
        if (subscribers.containsKey(topic))
            return false;
        subscribers.put(topic, ConcurrentHashMap.newKeySet());
        return true;
    }

    public void publish(String topic, Message message) {
        Set<Observer> subs = subscribers.get(topic);
        subs.parallelStream()
                .forEach(sub -> sub.update(message));
    }

    public boolean isSubscribed(Observer observer) {
        check(observer);
        return subscribers.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .anyMatch(set -> set.contains(observer));
    }

    public boolean isSubscribed(String topic, Observer observer) {
        check(observer);
        if (subscribers.containsKey(topic))
           return subscribers.get(topic).contains(observer);

        return false;
    }

    public boolean containsTopic(String topic) {
        return subscribers.containsKey(topic);
    }


    private void check(Observer observer) {
        if (observer == null)
            throw new IllegalArgumentException("Observer is null");
    }

}
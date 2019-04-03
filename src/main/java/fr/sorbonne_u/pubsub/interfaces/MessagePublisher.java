package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;

import java.util.function.Predicate;

public interface MessagePublisher {
    void sendMessage(Message message);

    default boolean accept(Message message) {
        return true;
    }

    default void setFilter(Topic topic, Predicate<Message> filter) {

    }

    default void shutdown() {

    }

    default String getKey() {
        return "";
    }
}

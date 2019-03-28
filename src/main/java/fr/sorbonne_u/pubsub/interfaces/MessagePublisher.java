package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Message;

public interface MessagePublisher {
    void sendMessage(Message message);

    default boolean accept(Message message) {
        return true;
    }
}

package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Message;

public interface Observer {
    void update(Message message);

    default boolean accept() {
        return true;
    }
}
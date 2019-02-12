package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;

@FunctionalInterface
public interface Observer {
    void update(Message message);
}

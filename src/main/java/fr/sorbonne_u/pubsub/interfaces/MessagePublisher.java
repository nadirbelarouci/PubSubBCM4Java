package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Message;

public interface MessagePublisher {
    void update(Message message) throws Exception;
}
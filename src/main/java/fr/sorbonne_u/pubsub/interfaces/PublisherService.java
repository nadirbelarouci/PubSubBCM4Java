package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Message;

public interface PublisherService {
    void publish(Message message) throws Exception;
}

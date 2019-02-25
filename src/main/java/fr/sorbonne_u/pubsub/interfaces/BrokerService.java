package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;

public interface BrokerService {
    void publish(Message message) throws Exception;

    void subscribe(Topic topic, MessageReceiver messageReceiver) throws Exception;

    void unsubscribe(Topic topic, MessageReceiver messageReceiver) throws Exception;

    void unsubscribe(MessageReceiver messageReceiver) throws Exception;
}

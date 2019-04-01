package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;

import java.util.function.Predicate;


public interface BrokerService {
    void publish(Message message) throws Exception;

    void subscribe(Topic topic, String subscriberPort) throws Exception;

    void subscribe(Topic topic, String subscriberPort, Predicate<Message> filter) throws Exception;

    void unsubscribe(Topic topic, String subscriberPort) throws Exception;

    void unsubscribe(String subscriberPort) throws Exception;

    void updateFilter(Topic topic, String subscriberPort, Predicate<Message> filter) throws Exception;

}

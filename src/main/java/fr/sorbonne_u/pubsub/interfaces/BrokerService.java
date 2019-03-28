package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;


public interface BrokerService {
    void publish(Message message) throws Exception;

    void subscribe(Topic topic, String subscriberPort) throws Exception;
    // TODO replace Filter with Predicate<Filter>
    void subscribe(Topic topic, String subscriberPort, Filter filter) throws Exception;

    void unsubscribe(Topic topic, String subscriberPort) throws Exception;

    void unsubscribe(String subscriberPort) throws Exception;

    void updateFilter(String subscriberPort, Filter filter) throws Exception;
    // TODO updateFilter with topic

}

package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Topic;

public interface SubscriberService {
    void subscribe(Topic topic) throws Exception ;
    // TODO replace Filter with Predicate<Filter>
    void subscribe(Topic topic, Filter filter) throws Exception;

    void unsubscribe(Topic topic) throws Exception;

    void unsubscribe() throws Exception ;
}

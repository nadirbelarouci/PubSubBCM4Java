package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Topic;

public interface SubscriberService {
    void subscribe(Topic topic) ;
    // TODO replace Filter with Predicate<Filter>
    void subscribe(Topic topic, Filter filter);


    void unsubscribe(Topic topic);

    void unsubscribe();
}

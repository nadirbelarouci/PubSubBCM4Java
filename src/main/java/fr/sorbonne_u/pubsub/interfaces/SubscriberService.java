package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.components.interfaces.RequiredI;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Topic;

public interface SubscriberService extends RequiredI {
    void subscribe(Topic topic);

    void subscribe(Topic topic, Filter filter);


    void unsubscribe(Topic topic);

    void unsubscribe();
}

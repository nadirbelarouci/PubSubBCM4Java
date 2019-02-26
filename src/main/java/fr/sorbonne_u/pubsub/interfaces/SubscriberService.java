package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.components.interfaces.RequiredI;
import fr.sorbonne_u.pubsub.Topic;

public interface SubscriberService extends RequiredI {
    void subscribe(Topic topic) throws Exception;

    void unsubscribe(Topic topic) throws Exception;

    void unsubscribe() throws Exception;
}

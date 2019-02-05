package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.components.interfaces.RequiredI;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;

public interface SubscriberI extends RequiredI {
    void subscribe(Topic topic);

    void unsubscribe(Topic topic);

    Message getMessage(Filter filter);

    Message getMessage();

    Iterable<Message> getAll();

    Iterable<Message> getAll(Filter filter);

}

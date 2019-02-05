package fr.sorbonne_u.pubsub.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.SubscriberI;

public class PubSubServiceConnector extends AbstractConnector implements SubscriberI {

    @Override
    public Message getMessage(Filter filter) {
        return null;
    }

    @Override
    public Message getMessage() {
        return null;
    }

    @Override
    public Iterable<Message> getAll() {
        return null;
    }

    @Override
    public Iterable<Message> getAll(Filter filter) {
        return null;
    }

    @Override
    public void subscribe(Topic topic) {

    }

    @Override
    public void unsubscribe(Topic topic) {

    }
}

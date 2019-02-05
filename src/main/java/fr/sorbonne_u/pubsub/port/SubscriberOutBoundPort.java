package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.SubscriberI;

public class SubscriberOutBoundPort extends AbstractOutboundPort implements SubscriberI {
    public SubscriberOutBoundPort(String uri, Class<?> implementedInterface, ComponentI owner) throws Exception {
        super(uri, implementedInterface, owner);
    }

    public SubscriberOutBoundPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
        super(implementedInterface, owner);
    }

    @Override
    public void subscribe(Topic topic) {

    }

    @Override
    public void unsubscribe(Topic topic) {

    }

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
}

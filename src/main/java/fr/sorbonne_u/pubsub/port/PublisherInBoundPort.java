package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.PublisherI;

public class PublisherInBoundPort extends AbstractInboundPort implements PublisherI {
    public PublisherInBoundPort(String uri, Class<?> implementedInterface, ComponentI owner) throws Exception {
        super(uri, implementedInterface, owner);
    }

    public PublisherInBoundPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
        super(implementedInterface, owner);
    }

    @Override
    public Message publish() {
        return null;
    }

    @Override
    public Iterable<Message> publishAll() {
        return null;
    }
}

package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.RequirableMessagePublisher;

public class MessagePublisherOutBoundPort extends AbstractOutboundPort implements RequirableMessagePublisher {

    public MessagePublisherOutBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, RequirableMessagePublisher.class, owner);
    }

    @Override
    public void update(Message message) throws Exception {
        ((RequirableMessagePublisher) this.connector).update(message);
    }
}

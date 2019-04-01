package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.RequirableMessagePublisher;

public class MessagePublisherOutBoundPort extends AbstractOutboundPort implements RequirableMessagePublisher {

    public MessagePublisherOutBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, RequirableMessagePublisher.class, owner);
    }

    public MessagePublisherOutBoundPort(ComponentI owner) throws Exception {
        super(RequirableMessagePublisher.class, owner);
    }

    @Override
    public void sendMessage(Message message) {
        ((RequirableMessagePublisher) this.connector).sendMessage(message);
    }
}

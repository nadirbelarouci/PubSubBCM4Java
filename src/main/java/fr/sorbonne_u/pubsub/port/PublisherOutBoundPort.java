package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.RequirablePublisherService;

public class PublisherOutBoundPort extends AbstractOutboundPort implements RequirablePublisherService {

    public PublisherOutBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, RequirablePublisherService.class, owner);
    }

    public PublisherOutBoundPort(ComponentI owner) throws Exception {
        super(RequirablePublisherService.class, owner);
    }

    @Override
    public void publish(Message message) throws Exception {
        ((RequirablePublisherService) this.connector).publish(message);
    }
}

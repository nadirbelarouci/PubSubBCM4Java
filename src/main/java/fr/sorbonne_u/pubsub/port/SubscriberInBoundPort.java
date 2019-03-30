package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.RequirableSubscriberService;

public class SubscriberInBoundPort extends AbstractInboundPort implements RequirableSubscriberService {

    public SubscriberInBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, RequirableSubscriberService.class, owner);
    }

    @Override
    public void subscribe(Topic topic) {

    }

    @Override
    public void subscribe(Topic topic, Filter filter) {

    }

    @Override
    public void unsubscribe(Topic topic) {

    }

    @Override
    public void unsubscribe() {

    }
}

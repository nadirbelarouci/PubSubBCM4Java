package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.RequirableSubscriberService;

public class SubscriberOutBoundPort extends AbstractOutboundPort implements RequirableSubscriberService {
    public SubscriberOutBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, RequirableSubscriberService.class, owner);
    }


    @Override
    public void subscribe(Topic topic) throws Exception {
        ((RequirableSubscriberService) this.connector).subscribe(topic);

    }

    @Override
    public void unsubscribe(Topic topic) throws Exception {
        ((RequirableSubscriberService) this.connector).unsubscribe(topic);

    }

    @Override
    public void unsubscribe() throws Exception {
        ((RequirableSubscriberService) this.connector).unsubscribe();
    }

}

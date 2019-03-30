package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.RequirableSubscriberService;

public class SubscriberOutBoundPort extends AbstractOutboundPort implements RequirableSubscriberService {
    // TODO SubscriberInBoundPort URI field and its Getter;
    private String subsriberInBoundPort;

    public SubscriberOutBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, RequirableSubscriberService.class, owner);
    }

    @Override
    public void subscribe(Topic topic)  {
        ((RequirableSubscriberService) this.connector).subscribe(topic);
    }

    @Override
    public void subscribe(Topic topic, Filter filter) {
        ((RequirableSubscriberService) this.connector).subscribe(topic, filter);
    }

    @Override
    public void unsubscribe(Topic topic)  {
        ((RequirableSubscriberService) this.connector).unsubscribe(topic);

    }

    @Override
    public void unsubscribe()  {
        ((RequirableSubscriberService) this.connector).unsubscribe();
    }

    public String getSubsriberInBoundPort() {
        return subsriberInBoundPort;
    }
}

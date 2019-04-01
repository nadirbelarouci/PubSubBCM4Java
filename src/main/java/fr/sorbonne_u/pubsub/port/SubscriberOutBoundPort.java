package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.RequirableSubscriberService;

import java.util.function.Predicate;

public class SubscriberOutBoundPort extends AbstractOutboundPort implements RequirableSubscriberService {
    private String subscriberInBoundPort;

    public SubscriberOutBoundPort(String outUri, ComponentI owner, String inUri) throws Exception {
        super(outUri, RequirableSubscriberService.class, owner);
        this.subscriberInBoundPort = inUri;
    }


    public SubscriberOutBoundPort(ComponentI owner, String inUri) throws Exception {
        super(RequirableSubscriberService.class, owner);
        this.subscriberInBoundPort = inUri;
    }

    public String getSubscriberInBoundPortURI() {
        return subscriberInBoundPort;
    }

    @Override
    public void subscribe(Topic topic) {
        ((RequirableSubscriberService) this.connector).subscribe(topic);
    }

    @Override
    public void subscribe(Topic topic, Predicate<Message> filter) {
        ((RequirableSubscriberService) this.connector).subscribe(topic, filter);
    }

    @Override
    public void updateFilter(Topic topic, Predicate<Message> filter) {
        ((RequirableSubscriberService) this.connector).updateFilter(topic, filter);

    }

    @Override
    public void unsubscribe(Topic topic) {
        ((RequirableSubscriberService) this.connector).unsubscribe(topic);
    }

    @Override
    public void unsubscribe() {
        ((RequirableSubscriberService) this.connector).unsubscribe();
    }

}

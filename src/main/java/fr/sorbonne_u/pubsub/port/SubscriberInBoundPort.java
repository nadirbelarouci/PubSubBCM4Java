package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.connectors.ConnectorI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.components.Subscriber;
import fr.sorbonne_u.pubsub.interfaces.RequirableSubscriberService;

public class SubscriberInBoundPort extends AbstractInboundPort implements RequirableSubscriberService {

    SubscriberOutBoundPort subscriberOutBoundPort;

    public SubscriberInBoundPort(String uri, ComponentI owner, SubscriberOutBoundPort subscriberOutBoundPort) throws Exception {
        super(uri, RequirableSubscriberService.class, owner);
        this.subscriberOutBoundPort = subscriberOutBoundPort;
        this.subscriberOutBoundPort.setSubsriberInBoundPort(uri);
    }

    @Override
    public void subscribe(Topic topic) throws Exception{
        this.doConnection(subscriberOutBoundPort.getPortURI(), (ConnectorI) this.owner);

    }

    @Override
    public void subscribe(Topic topic, Filter filter) throws Exception {
        this.doConnection(subscriberOutBoundPort.getPortURI(), (ConnectorI) this.owner);
    }

    @Override
    public void unsubscribe(Topic topic) throws Exception {
        throw new Exception("Can't unsubscribe "
                + " in an inbound port!") ;
    }

    @Override
    public void unsubscribe() throws Exception {
        throw new Exception("Can't unsubscribe "
                + " in an inbound port!") ;

    }
}

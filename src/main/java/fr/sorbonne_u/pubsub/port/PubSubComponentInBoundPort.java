package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.BrokerService;
import fr.sorbonne_u.pubsub.interfaces.OfferableBrokerService;

public class PubSubComponentInBoundPort extends AbstractInboundPort implements OfferableBrokerService {
    public PubSubComponentInBoundPort(String inBoundPortUri, ComponentI owner) throws Exception {
        super(inBoundPortUri, OfferableBrokerService.class, owner);
    }


    @Override
    public void publish(Message message) throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerService) this.getOwner()).publish(message);
                        return null;
                    }
                });
    }

    @Override
    public void subscribe(Topic topic, String subscriberPort) throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerService) this.getOwner()).subscribe(topic, subscriberPort);
                        return null;
                    }
                });
    }

    @Override
    public void subscribe(Topic topic, String subscriberPort, Filter filter) throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerService) this.getOwner()).subscribe(topic, subscriberPort, filter);
                        return null;
                    }
                });
    }

    @Override
    public void updateFilter(String subscriberPort, Filter filter) throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerService) this.getOwner()).updateFilter(subscriberPort, filter);
                        return null;
                    }
                });
    }

    @Override
    public void unsubscribe(Topic topic, String subscriberPort) throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerService) this.getOwner()).unsubscribe(topic, subscriberPort);
                        return null;
                    }
                });
    }

    @Override
    public void unsubscribe(String subscriberPort) throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((BrokerService) this.getOwner()).unsubscribe(subscriberPort);
                        return null;
                    }
                });
    }
}

package fr.sorbonne_u.pubsub.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.OfferableBrokerService;
import fr.sorbonne_u.pubsub.interfaces.RequirableSubscriberService;
import fr.sorbonne_u.pubsub.port.SubscriberOutBoundPort;

import java.util.function.Predicate;

public class SubscriberServiceConnector extends AbstractConnector implements RequirableSubscriberService {

    private String subscriberInBoundPortURI = null;

    private String getSubscriberInBoundPortURI() {
        if (subscriberInBoundPortURI != null)
            return subscriberInBoundPortURI;

        if (!(this.requiring instanceof SubscriberOutBoundPort))
            throw new IllegalStateException();

        subscriberInBoundPortURI = ((SubscriberOutBoundPort) this.requiring).getSubscriberInBoundPortURI();
        return subscriberInBoundPortURI;
    }

    @Override
    public void subscribe(Topic topic) {

        try {
            ((OfferableBrokerService) this.offering).subscribe(topic, getSubscriberInBoundPortURI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void subscribe(Topic topic, Predicate<Message> filter) {
        try {
            ((OfferableBrokerService) this.offering).subscribe(topic, getSubscriberInBoundPortURI(), filter);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void updateFilter(Topic topic, Predicate<Message> filter) {
        try {
            ((OfferableBrokerService) this.offering).updateFilter(topic, getSubscriberInBoundPortURI(), filter);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void unsubscribe(Topic topic) {
        try {
            ((OfferableBrokerService) this.offering).unsubscribe(topic, getSubscriberInBoundPortURI());
        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }

    @Override
    public void unsubscribe() {
        try {
            ((OfferableBrokerService) this.offering).unsubscribe(this.requiringPortURI);

        } catch (Exception e) {
            throw new RuntimeException(e);

        }

    }
}

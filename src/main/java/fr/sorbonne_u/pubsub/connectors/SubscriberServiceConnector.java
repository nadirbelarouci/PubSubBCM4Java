package fr.sorbonne_u.pubsub.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.OfferableBrokerService;
import fr.sorbonne_u.pubsub.interfaces.RequirableSubscriberService;
import fr.sorbonne_u.pubsub.port.SubscriberOutBoundPort;

public class SubscriberServiceConnector extends AbstractConnector implements RequirableSubscriberService {

    @Override
    public void subscribe(Topic topic) {
        if (!(this.requiring instanceof SubscriberOutBoundPort))
            throw new IllegalStateException();

        String port = ((SubscriberOutBoundPort) this.requiring).getSubsriberInBoundPort();
        try {
            ((OfferableBrokerService) this.offering).subscribe(topic, this.requiringPortURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void subscribe(Topic topic, Filter filter) {
        try {
            ((OfferableBrokerService) this.offering).subscribe(topic, this.requiringPortURI, filter);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void unsubscribe(Topic topic) {
        try {
            ((OfferableBrokerService) this.offering).unsubscribe(topic, this.requiringPortURI);
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

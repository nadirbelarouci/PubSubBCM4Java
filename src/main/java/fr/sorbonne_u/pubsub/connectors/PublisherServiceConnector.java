package fr.sorbonne_u.pubsub.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.OfferableBrokerService;
import fr.sorbonne_u.pubsub.interfaces.RequirablePublisherService;

public class PublisherServiceConnector extends AbstractConnector implements RequirablePublisherService {


    @Override
    public void publish(Message message) throws Exception {
        ((OfferableBrokerService) this.offering).publish(message);
    }
}

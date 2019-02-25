package fr.sorbonne_u.pubsub.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.MessageReceiver;
import fr.sorbonne_u.pubsub.interfaces.OfferableBrokerService;
import fr.sorbonne_u.pubsub.interfaces.RequirableSubscriberService;

public class SubscriberServiceConnector extends AbstractConnector implements RequirableSubscriberService {

    @Override
    public void subscribe(Topic topic) throws Exception {
        ((OfferableBrokerService) this.offering).subscribe(topic, (MessageReceiver) this.requiring);

    }

    @Override
    public void unsubscribe(Topic topic) throws Exception {
        ((OfferableBrokerService) this.offering).unsubscribe(topic, (MessageReceiver) this.requiring);

    }

    @Override
    public void unsubscribe() throws Exception {
        ((OfferableBrokerService) this.offering).unsubscribe((MessageReceiver) this.requiring);

    }
}

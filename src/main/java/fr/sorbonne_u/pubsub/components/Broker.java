package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.pubsub.interfaces.PublisherI;
import fr.sorbonne_u.pubsub.interfaces.SubscriberI;

@RequiredInterfaces(required = SubscriberI.class)
@OfferedInterfaces(offered = PublisherI.class)
public class Broker extends AbstractComponent {
    public Broker(String prefix, String BrokerURI) throws Exception {
        super(prefix, 0, 1);
    }
}

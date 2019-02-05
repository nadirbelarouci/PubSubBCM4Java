package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.pubsub.interfaces.SubscriberI;

@RequiredInterfaces(required = SubscriberI.class)
public class Suscriber extends AbstractComponent {
    public Suscriber(String uri, String subscriberURI) throws Exception {
        super(uri, 0, 1);
    }
}

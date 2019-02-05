package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.PublisherI;

@OfferedInterfaces(offered = PublisherI.class)
public class Publisher extends AbstractComponent {
    public Publisher(String uri, String publisherURI) throws Exception {
        super(uri, 0, 1);
    }


    public Message publishService() {

        return null;
    }

    public Iterable<Message> publishAllService() {
        return null;
    }
}

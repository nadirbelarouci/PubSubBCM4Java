package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.MessagePublisher;
import fr.sorbonne_u.pubsub.interfaces.RequirableMessagePublisher;
import fr.sorbonne_u.pubsub.interfaces.RequirablePublisherService;
import fr.sorbonne_u.pubsub.port.MessagePublisherOutBoundPort;

import java.util.Objects;

@RequiredInterfaces(required = RequirableMessagePublisher.class)
public class MessagePublisherComponent  implements MessagePublisher {
    // TODO extends component
    // TODO add MessagePublisherOutBoundPort
    // TODO add the connection between this outBoundPort and subscriberInBoundPort after this creating component
    // TODO handle the Predicate<Filter>

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public boolean accept(Message message) {
        return false;
    }
}

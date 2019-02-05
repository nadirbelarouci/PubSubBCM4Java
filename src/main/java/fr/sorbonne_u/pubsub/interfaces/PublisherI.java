package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.pubsub.Message;

public interface PublisherI extends OfferedI {
    Message publish();

    Iterable<Message> publishAll();
}

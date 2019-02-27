package fr.sorbonne_u.pubsub.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.MessageReceiver;
import fr.sorbonne_u.pubsub.interfaces.RequirableMessagePublisher;

public class MessagePublisherServiceConnector extends AbstractConnector implements RequirableMessagePublisher {

    @Override
    public void sendMessage(Message message) {
        try {
            ((MessageReceiver) this.offering).receiveMessage(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

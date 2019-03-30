package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.OfferableMessageReceiver;

public class MessageReceiverOutBoundPort extends AbstractOutboundPort
implements OfferableMessageReceiver{
    public MessageReceiverOutBoundPort(String outBoundPortUri, ComponentI owner) throws Exception {
        super(outBoundPortUri, OfferableMessageReceiver.class, owner);
    }

    @Override
    public void receiveMessage(Message message) throws Exception {

    }
}

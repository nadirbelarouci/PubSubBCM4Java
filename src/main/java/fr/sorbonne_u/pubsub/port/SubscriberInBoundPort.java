package fr.sorbonne_u.pubsub.port;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.MessageReceiver;
import fr.sorbonne_u.pubsub.interfaces.OfferableMessageReceiver;

public class SubscriberInBoundPort extends AbstractInboundPort implements OfferableMessageReceiver {
    public SubscriberInBoundPort(String inBoundPortUri, ComponentI owner) throws Exception {
        super(inBoundPortUri, OfferableMessageReceiver.class, owner);
    }


    @Override
    public void receiveMessage(Message message)throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((MessageReceiver) this.getOwner()).receiveMessage(message);
                        return null;
                    }
                });
    }
}

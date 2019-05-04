package fr.sorbonne_u.components.pubsub.port;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.interfaces.Observer;

/**
 * The {@code PublisherOutBoundPort} is an implementation of the
 * Subscriber in-bound port in order to receive messages.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.components.Subscriber
 */
public class SubscriberInBoundPort extends AbstractInboundPort implements Observer.Offered {
    /**
     * Creates a new {@code SubscriberInBoundPort} instance with a specific URI.
     *
     * @param uri   The port URI
     * @param owner The component owner
     * @throws Exception
     */
    public SubscriberInBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, Observer.Offered.class, owner);
    }

    /**
     * Creates a new {@code SubscriberInBoundPort} instance with a random unique URI.
     *
     * @param owner The component owner
     * @throws Exception
     */
    public SubscriberInBoundPort(ComponentI owner) throws Exception {
        super(Observer.Offered.class, owner);
    }

    @Override
    public void notify(Message message) throws Exception {
        this.getOwner().handleRequestSync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((Observer) this.getOwner()).notify(message);
                        return null;
                    }
                });
    }
}

package fr.sorbonne_u.components.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.interfaces.Observer;

/**
 * The {@code PubSubOutBoundPort} is an implementation of the PubSub out-bound port.
 *
 * <p>
 * Unlike the in-bound port, this PubSub has as many out-bound ports as subscribers.
 * Since the {@code Subscribable} interface is the responsible of holding subscribers in the PubSub,
 * each {@code Subscribable} instance must hold a reference to an out-bound port so it can notify a subscriber.
 * </p>
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.components.PubSub
 * @see fr.sorbonne_u.components.pubsub.interfaces.Subscribable
 */
public class PubSubOutBoundPort extends AbstractOutboundPort implements Observer.Required {
    /**
     * Creates a new {@code PubSubOutBoundPort} instance with a specific URI.
     *
     * @param uri   The port URI
     * @param owner The component owner
     * @throws Exception
     */
    public PubSubOutBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, Observer.Required.class, owner);
    }

    /**
     * Creates a new {@code PubSubOutBoundPort} instance with a random unique URI.
     *
     * @param owner The component owner
     * @throws Exception
     */
    public PubSubOutBoundPort(ComponentI owner) throws Exception {
        super(Observer.Required.class, owner);
    }

    /**
     * Notify a subscriber.
     *
     * @param message A  {@code Message}
     * @throws Exception
     */
    @Override
    public void notify(Message message) throws Exception {
        ((Observer.Required) this.connector).notify(message);
    }


}

package fr.sorbonne_u.components.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.interfaces.PublisherService;

/**
 * The {@code PublisherOutBoundPort} is an implementation of the Publisher out-bound port.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.components.Publisher
 */
public class PublisherOutBoundPort extends AbstractOutboundPort implements PublisherService.Required {

    /**
     * Creates a new {@code PublisherOutBoundPort} instance with a specific URI.
     *
     * @param uri The port URI
     * @param owner           The component owner
     * @throws Exception
     */
    public PublisherOutBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, PublisherService.Required.class, owner);
    }

    /**
     * Creates a new {@code PublisherOutBoundPort} instance with a random unique URI.
     *
     * @param owner The component owner
     * @throws Exception
     */
    public PublisherOutBoundPort(ComponentI owner) throws Exception {
        super(PublisherService.Required.class, owner);
    }

    @Override
    public void publish(Message message) {
        ((PublisherService.Required) this.connector).publish(message);
    }
}

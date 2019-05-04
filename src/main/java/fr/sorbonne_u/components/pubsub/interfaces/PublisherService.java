package fr.sorbonne_u.components.pubsub.interfaces;

import fr.sorbonne_u.components.interfaces.RequiredI;
import fr.sorbonne_u.components.pubsub.Message;

/**
 * The {@code PublisherService} interface defines the contract that a {@code Publisher Component} must uphold.
 *
 * <p>
 * The interface declares a Required sub-interface which implements the {@link RequiredI}
 * interface since it will be implemented by a publisher out-bound port.
 * </p>
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.port.PublisherOutBoundPort
 */
public interface PublisherService {

    /**
     * Publish a message to the PubSub.
     *
     * @param message A {@code Message}
     */
    void publish(Message message);


    /**
     * A required interface
     */
    interface Required extends PublisherService, RequiredI {

    }
}

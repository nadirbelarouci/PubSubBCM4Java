package fr.sorbonne_u.components.pubsub.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.interfaces.RequiredI;
import fr.sorbonne_u.components.pubsub.Message;

/**
 * A class can implement the {@code Observer} interface when it
 * wants to be informed of changes in observable objects.
 *
 * <p>
 * The {@code Observer} interface declares two other sub-interfaces, an offered one, and a required one.
 * <ul>
 * <il>
 * <p>
 * The {@code Offered} interface which implements the {@link OfferedI}
 * interface will be used by components that offers in-bound ports such as Subscribers.
 * </il>
 * <il>
 * The {@code Required} interface which implements the {@link RequiredI} will be used by components
 * that requires out-bound ports,these out-bound ports eventually will call the offered interface {@link #notify} method.
 * </il>
 * </ul>
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.port.SubscriberInBoundPort
 * @see fr.sorbonne_u.components.pubsub.port.PubSubOutBoundPort
 * @see Subscription
 */
public interface Observer {
    /**
     * This method is called whenever the observed object receives a message.
     *
     * @param message A {@code Message}
     * @throws Exception
     */
    void notify(Message message) throws Exception;


    /**
     * An Offered interface
     */
    interface Offered extends Observer, OfferedI {

    }

    /**
     * A Required interface
     */
    interface Required extends Observer, RequiredI {

    }
}

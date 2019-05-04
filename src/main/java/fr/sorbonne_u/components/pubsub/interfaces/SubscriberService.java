package fr.sorbonne_u.components.pubsub.interfaces;

import fr.sorbonne_u.components.interfaces.RequiredI;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;

import java.util.function.Predicate;

/**
 * The {@code SubscriberService} interface defines the contract that a {@code Subscriber Component} must uphold.
 *
 * <p>
 * The interface declares a Required sub-interface which implements the {@link RequiredI}
 * interface since it will be implemented by a subscriber out-bound port.
 * </p>
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.port.SubscriberOutBoundPort
 */
public interface SubscriberService {

    /**
     * Subscribe to a topic.
     *
     * @param topic A {@code Topic}
     */
    void subscribe(Topic topic);


    /**
     * Subscribe to a topic using a filter.
     *
     * @param topic  A {@code Topic}
     * @param filter A predicate of a message
     */
    void subscribe(Topic topic, Predicate<Message> filter);

    /**
     * Unsubscribe from a topic.
     *
     * @param topic A {@code Topic}
     */
    void unsubscribe(Topic topic);

    /**
     * Unsubscribe from all topics.
     */
    void unsubscribe();


    /**
     * Update a filter for a topic or add a new one.
     *
     * @param topic A {@code Topic}
     */
    void filter(Topic topic, Predicate<Message> filter);

    /**
     * Returns the in-bound port URI of the subscriber.
     */
    String getSubId();


    /**
     * A required interface
     */
    interface Required extends SubscriberService, RequiredI {

    }
}

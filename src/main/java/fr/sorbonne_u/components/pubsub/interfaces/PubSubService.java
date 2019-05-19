package fr.sorbonne_u.components.pubsub.interfaces;

import fr.sorbonne_u.components.interfaces.OfferedI;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;

import java.util.function.Predicate;

/**
 * The {@code PubsubService} interface defines the contract that a {@code PubSub Component} must uphold.
 *
 * <p>
 * The interface declares an Offered sub-interface which implements the {@link OfferedI}
 * interface since it will be implemented by a pubsub in-bound port.
 * </p>
 *
 * @see fr.sorbonne_u.components.pubsub.port.PubSubInBoundPort
 */
public interface PubSubService {

    /**
     * Publish a message to subscribers
     *
     * @param message A {@code Message}
     * @throws Exception remote exception
     */
    void publish(Message message) throws Exception;

    /**
     * Subscribe to topic by creating a {@link Subscription} instance from the Subscriber ID.
     *
     * @param subId The subscriber ID
     * @param topic A {@code Topic}
     * @throws Exception remote exception
     */
    void subscribe(String subId, Topic topic) throws Exception;

    /**
     * Subscribe to topic with a filter by creating a {@link Subscription} instance from the Subscriber ID.
     *
     * @param subId  The subscriber ID
     * @param topic  A {@code Topic}
     * @param filter A predicate of a message
     * @throws Exception remote exception
     */

    void subscribe(String subId, Topic topic, Predicate<Message> filter) throws Exception;

    /**
     * Unsubscribe from a topic.
     *
     * @param subId The subscriber ID
     * @param topic A {@code Topic}
     * @throws Exception remote exception
     */
    void unsubscribe(String subId, Topic topic) throws Exception;


    /**
     * Unsubscribe from a all topics.
     *
     * @param subId The subscriber ID
     * @throws Exception remote exception
     */
    void unsubscribe(String subId) throws Exception;


    /**
     * Update a subscriber filter for a given topic or add a new one.
     *
     * @param subId The subscriber ID
     * @param topic A {@code Topic}
     * @throws Exception remote exception
     */
    void filter(String subId, Topic topic, Predicate<Message> filter) throws Exception;

    /**
     * An offered interface
     */
    interface Offered extends PubSubService, OfferedI {

    }
}

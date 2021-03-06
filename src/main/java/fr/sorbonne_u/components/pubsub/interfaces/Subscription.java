package fr.sorbonne_u.components.pubsub.interfaces;

import fr.sorbonne_u.components.pubsub.Message;

import java.util.function.Predicate;

/**
 * The {@code PubSub}  must keep track of the subscribers that it needs to notify at each publication, hence
 * the {@code Subscription} interface is just an abstraction of a subscriber.
 * <p>
 * Note that this interface role is totally different from the {@link SubscriberService} interface role.
 * This interface will be used by the PubSub to keep track of the subscribers, i.e for each subscription there will be
 * a required out-bound port ({@link Observer.Required}) to notify a subscriber, not only that,
 * but also to update the subscription's filter.
 * However the {@code SubscriberService} interface role defines the required services
 * that a subscriber can perform, such as subscribe or unsubscribe.
 * </p>
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see Observer
 * @see SubscriberService
 * @see fr.sorbonne_u.components.pubsub.port.PubSubOutBoundPort
 */
public interface Subscription {

    /**
     * Notify a subscriber
     *
     * @param message A {@code Message}
     */
    void notify(Message message);

    /**
     * Update a Subscription filter
     *
     * @param filter A predicate of a message
     */
    void filter(Predicate<Message> filter);


    /**
     * Returns the subscriber ID.
     *
     * @return The subscriber ID
     */
    String getSubId();


    /**
     * Clean and shut any used resources such as ports or threads.
     */
    void end();

}

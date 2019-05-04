package fr.sorbonne_u.components.pubsub.components;

import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;
import fr.sorbonne_u.components.pubsub.interfaces.Subscribable;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * A {@code Broker} is the heart of a {@code PubSub} component, in fact The {@code PubSub} class
 * delegate all its operations to the {@code Broker} class.
 *
 * <p>
 * The {@code Broker} class needs to maximize the parallelism of handling asynchronously publishing and
 * subscribing requests from other components and it does so by using two executors:
 * <ol>
 * <li>
 * {@code PublisherExecutor}: This executor handles publishing messages to the subscribers.
 * </li>
 * <li>
 * {@code SubscriberExecutor}: This executor handles all requests coming from
 * subscribers such as subscribe, unsubscribe etc.
 * </li>
 * </ol>
 * </p>
 *
 * <p>
 * All this class methods could have been embedded directly in the {@code PubSub}
 * component, but if we did so, the class would become hard to test.
 * Since all requests are handled asynchronously, returning a {@code CompletableFuture}
 * for each method is an obligation to maintain testing the features of this class.
 * </p>
 *
 * <p>
 * Note that the class is not responsible for creating {@code Subscribable} instances,
 * it is up to the user of this class to create them, i.e a {@code PubSub} class, or a test class
 * </p>
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see PubSub
 * @see PublisherExecutor
 * @see SubscriberExecutor
 */
public class Broker {
    /**
     * An executor for handling message publishing.
     */
    private final PublisherExecutor publisherExecutor;
    /**
     * An executor for handling subscriber requests.
     */
    private final SubscriberExecutor subscriberExecutor;

    /**
     * Creates a new broker instance with default parallelism equals to <b>10</b> for both the
     * {@link #subscriberExecutor} and the {@link #publisherExecutor}.
     */
    protected Broker() {
        this.publisherExecutor = new PublisherExecutor();
        this.subscriberExecutor = new SubscriberExecutor();
    }

    /**
     * Creates a new broker instance with specific parallelism values  for both the
     * {@link #subscriberExecutor} and the {@link #publisherExecutor}.
     *
     * @param subscribingParallelism The subscriberExecutor parallelism
     * @param publishingParallelism  The publisherExecutor parallelism
     */
    protected Broker(int subscribingParallelism, int publishingParallelism) {
        this.subscriberExecutor = new SubscriberExecutor(subscribingParallelism);
        this.publisherExecutor = new PublisherExecutor(publishingParallelism);
    }

    /**
     * Publish a message to subscribers.
     *
     * @param message A {@code Message}
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> publish(Message message) {
        Objects.requireNonNull(message, "Message cannot be null");
        return publisherExecutor.publish(message, subscriberExecutor.getSubscribers(message.getTopic()));
    }

    /**
     * Subscribe to a topic.
     *
     * @param sub   A {@code Subscribable}
     * @param topic A {@code Topic}
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> subscribe(Subscribable sub, Topic topic) {
        Objects.requireNonNull(sub, "Subscriber cannot be null.");
        Objects.requireNonNull(topic, "Topic cannot be null.");

        return subscriberExecutor.subscribe(sub, topic);
    }


    /**
     * Subscribe to a topic using a filter
     *
     * @param sub    A {@code Subscribable}
     * @param topic  A {@code Topic}
     * @param filter A predicate of a message
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> subscribe(Subscribable sub, Topic topic, Predicate<Message> filter) {
        Objects.requireNonNull(sub, "Subscriber cannot be null.");
        Objects.requireNonNull(topic, "Topic cannot be null.");
        Objects.requireNonNull(filter, "Filter cannot be null.");
        sub.filter(topic, filter);
        return subscriberExecutor.subscribe(sub, topic);
    }

    /**
     * Unsubscribe from a topic.
     *
     * @param sub   A {@code Subscribable}
     * @param topic A {@code Topic}
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> unsubscribe(Subscribable sub, Topic topic) {
        Objects.requireNonNull(sub, "Subscriber cannot be null.");
        Objects.requireNonNull(topic, "Topic cannot be null.");
        return subscriberExecutor.unsubscribe(sub, topic);
    }

    /**
     * Unsubscribe from a topic using the subscriber ID.
     *
     * @param subId The subscriber ID.
     * @param topic A {@code Topic}
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> unsubscribe(String subId, Topic topic) {
        Objects.requireNonNull(subId, "Subscriber cannot be null.");
        Objects.requireNonNull(topic, "Topic cannot be null.");
        return subscriberExecutor.unsubscribe(subId, topic);
    }

    /**
     * Unsubscribe from all topics.
     *
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> unsubscribe(Subscribable sub) {
        Objects.requireNonNull(sub, "Subscriber cannot be null.");
        return subscriberExecutor.unsubscribe(sub);
    }

    /**
     * Unsubscribe from all topic using the subscriber ID.
     *
     * @param subId The subscriber ID.
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> unsubscribe(String subId) {
        Objects.requireNonNull(subId, "Subscriber cannot be null.");
        return subscriberExecutor.unsubscribe(subId);
    }


    /**
     * Update a subscriber filter for a given topic or add a new one.
     *
     * @param subId  The subscriber ID.
     * @param topic  A {@code Topic}
     * @param filter A predicate of a message
     * @return A {@code CompletableFuture} for this async request
     */

    protected CompletableFuture<Void> filter(String subId, Topic topic, Predicate<Message> filter) {
        Objects.requireNonNull(subId, "Subscriber cannot be null.");
        Objects.requireNonNull(topic, "Topic cannot be null.");
        Objects.requireNonNull(filter, "Filter cannot be null.");

        return subscriberExecutor.filter(subId, topic, filter);
    }

    /**
     * Delete a topic.
     * <p>
     * When deleting a topic it is not guaranteed that all the subscribers to this topic will get
     * the messages published on this topic at this moment.
     * </p>
     *
     * @param topic A {@code Topic}
     * @return A {@code CompletableFuture} for this async request
     */

    protected CompletableFuture<Void> removeTopic(Topic topic) {
        Objects.requireNonNull(topic, "Topic cannot be null.");
        return subscriberExecutor.removeTopic(topic);
    }

    /**
     * Get all topics.
     *
     * @return a set of topics
     */
    protected Set<Topic> geTopics() {
        return subscriberExecutor.getTopics();
    }

    /**
     * Check if a topic exists.
     *
     * @param topic A {@code Topic}
     * @return true if the topic exists.
     */
    protected boolean hasTopic(Topic topic) {
        Objects.requireNonNull(topic, "Topic cannot be null.");
        return subscriberExecutor.hasTopic(topic);
    }

    /**
     * Check if a subscriber is subscribed any topic.
     *
     * @param sub A {@code Subscribable}
     * @return true if there is a topic on which {@code sub} is subscribed to
     */
    protected boolean isSubscribed(Subscribable sub) {
        Objects.requireNonNull(sub, "Subscriber cannot be null.");
        return subscriberExecutor.isSubscribed(sub);
    }

    /**
     * Check if a subscriber is subscribed to a specific topic.
     *
     * @param sub   A {@code Subscribable}
     * @param topic A {@code Topic}
     * @return true if {@code sub} is subscribed to {@code topic}
     */
    protected boolean isSubscribed(Subscribable sub, Topic topic) {
        Objects.requireNonNull(sub, "Subscriber cannot be null.");
        Objects.requireNonNull(topic, "Topic cannot be null.");
        return subscriberExecutor.isSubscribed(sub, topic);
    }

    /**
     * Shutdown this broker executors.
     */
    protected void shutdown() {
        publisherExecutor.shutdown();
        subscriberExecutor.shutdown();
    }


}

package fr.sorbonne_u.components.pubsub.components;

import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;
import fr.sorbonne_u.components.pubsub.interfaces.Subscribable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * The {@code SubscriberExecutor} handles subscriber related requests for the {@code Broker}.
 * Subscribers are organized in a {@code Map} which has topics as keys and
 * a collection of subscribers to that topic as a value.
 * <p>
 * Since the class manipulates {@code Subscribable} instances,
 * and since each {@code Subscribable} instance has a unique ID,
 * the collection of subscribers that are subscribed to a topic is also
 * a {@code Map} which has {@code SubId} as a key and the
 * {@code Subscribable} instance itself as a value.
 * <p>
 * In other words, to access a {@code Subscribable} instance we need either:
 * <ul>
 * <li>
 * A topic and a {@code Subscribable} instance.
 * </li>
 * <li>
 * A topic and a {@code Subscribable} ID.
 * </li>
 * </ul>
 * <p>
 * To maximize concurrency, the class uses a {@link ConcurrentHashMap},
 * please check out its documentation to understand more about the mechanisms that it uses.
 *
 * @author Nadir Belarocui
 * @author Katia Amichi
 * @see SubscriberExecutor
 * @see ConcurrentHashMap
 */
public class SubscriberExecutor extends HandlerExecutor {
    private final ConcurrentHashMap<Topic, ConcurrentHashMap<String, Subscribable>> subscribers = new ConcurrentHashMap<>();

    /**
     * Create a defalut executor with parallelism equals to 10.
     */
    protected SubscriberExecutor() {
        super();
    }

    /**
     * Create an executor with a specific parallelism value.
     *
     * @param parallelism An {@code int} value
     */
    protected SubscriberExecutor(int parallelism) {
        super(parallelism);
    }

    /**
     * Get a collection of subscribers that are subscribed to a specific topic.
     * If the {@link #subscribers} map contains a {@code ROOT} topic then:
     * <ul>
     * <li>
     * {@code ROOT} is the only topic.
     * </li>
     * <li>
     * {@code Subscribable} instances that are subscribed to ROOT are {@code PubSubNode} subscribers.
     * </li>
     * </ul>
     *
     * @param topic A {@code Topic}
     * @return A collection of {@code Subscribable} instances.
     */
    protected Collection<Subscribable> getSubscribers(Topic topic) {
        if (subscribers.containsKey(Topic.ROOT)) {
            return subscribers.get(Topic.ROOT).values();
        }
        Map<String, Subscribable> subs = subscribers.get(topic);
        return subs == null ? null : subs.values();
    }

    /**
     * Subscribe to a topic.
     *
     * @param sub   A {@code Subscribable}
     * @param topic A {@code Topic}
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> subscribe(Subscribable sub, Topic topic) {
        return runAsync(() -> addSubscriber(sub, topic));
    }


    private void addSubscriber(Subscribable sub, Topic topic) {
        // if topic is absent, then initialize a new ConcurrentHashMap for that topic
        // and put the subscriber in it
        subscribers.computeIfAbsent(topic, t -> new ConcurrentHashMap<>())
                .put(sub.getSubId(), sub);
    }

    /**
     * Unsubscribe from a topic.
     *
     * @param sub   A {@code Subscribable}
     * @param topic A {@code Topic}
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> unsubscribe(Subscribable sub, Topic topic) {
        return unsubscribe(sub.getSubId(), topic);
    }

    /**
     * Unsubscribe from a topic.
     *
     * @param subId A {@code Subscribable} ID
     * @param topic A {@code Topic}
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> unsubscribe(String subId, Topic topic) {
        return runAsync(() -> deleteSubscriber(subId, topic));
    }

    /**
     * Unsubscribe from all topics.
     *
     * @param sub A {@code Subscribable}
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> unsubscribe(Subscribable sub) {
        return unsubscribe(sub.getSubId());
    }

    /**
     * Unsubscribe from all topics.
     *
     * @param subId A {@code Subscribable} ID
     * @return A {@code CompletableFuture} for this async request
     */
    protected CompletableFuture<Void> unsubscribe(String subId) {
        return runAsync(() -> deleteSubscriber(subId));
    }

    private void deleteSubscriber(String subId, Topic topic) {
        // delete subscriber with one atomic block
        subscribers.computeIfPresent(topic, (t, subs) -> {
            Subscribable sub = subs.remove(subId);
            if (sub != null)
                sub.shutdown();
            return subs;
        });
    }

    private void deleteSubscriber(String subId) {
        // delete subscriber in parallel from all subscribers
        subscribers.keySet()
                .parallelStream()
                .forEach(topic -> deleteSubscriber(subId, topic));
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
        return runAsync(() -> addFilter(subId, topic, filter));
    }


    private void addFilter(String subId, Topic topic, Predicate<Message> filter) {
        // add the filter or update it
        subscribers.computeIfPresent(topic, (t, subs) -> {
            subs.computeIfPresent(subId, (id, sub) -> {
                sub.filter(topic, filter);
                return sub;
            });
            return subs;
        });

    }

    /**
     * Check if a subscriber is subscribed any topic.
     *
     * @param sub A {@code Subscribable}
     * @return true if there is a topic on which {@code sub} is subscribed to
     */

    protected boolean isSubscribed(Subscribable sub) {
        return subscribers.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .anyMatch(subs -> subs.containsKey(sub.getSubId()));

    }

    /**
     * Check if a subscriber is subscribed to a specific topic.
     *
     * @param sub   A {@code Subscribable}
     * @param topic A {@code Topic}
     * @return true if {@code sub} is subscribed to {@code topic}
     */

    protected boolean isSubscribed(Subscribable sub, Topic topic) {
        ConcurrentHashMap<String, Subscribable> subs = subscribers.get(topic);
        return subs != null && subs.containsKey(sub.getSubId());
    }

    /**
     * Check if a topic exists.
     *
     * @param topic A {@code Topic}
     * @return true if the topic exists.
     */
    protected boolean hasTopic(Topic topic) {
        return subscribers.containsKey(topic);
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
        return runAsync(() -> subscribers.remove(topic));
    }

    /**
     * Shutdown this  executor and clear subscribers.
     */
    @Override
    protected void shutdown() {
        subscribers.values().forEach(subs -> subs.values().forEach(Subscribable::shutdown));
        subscribers.clear();
        super.shutdown();
    }

    /**
     * Get all topics.
     *
     * @return a set of topics
     */
    protected Set<Topic> getTopics() {
        return subscribers.keySet();
    }
}

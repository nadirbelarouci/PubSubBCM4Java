package fr.sorbonne_u.components.pubsub.components;

import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.interfaces.Subscription;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The {@code PublisherExecutor} handles message publishing for the {@code Broker}.
 *
 * @author Nadir Belarocui
 * @author Katia Amichi
 * @see SubscriberExecutor
 */
public class PublisherExecutor extends HandlerExecutor {
    /**
     * Create a defalut executor with parallelism equals to 10.
     */
    protected PublisherExecutor() {
        super();
    }

    /**
     * Create an executor with a specific parallelism value.
     *
     * @param parallelism An {@code int} value
     */
    protected PublisherExecutor(int parallelism) {
        super(parallelism);
    }

    /**
     * Publish the message asynchronously.
     *
     * @param message     A {@code Message}
     * @param subscribers A collection of subscribers
     * @return a {@code CompletableFuture}
     */
    protected CompletableFuture<Void> publish(Message message, Collection<Subscription> subscribers) {
        return runAsync(() -> sendMessage(message, subscribers));
    }

    /**
     * Sends the messages in parallel to the subscribers.
     *
     * @param message     A {@code Message}
     * @param subscribers A collection of subscribers
     */
    private void sendMessage(Message message, Collection<Subscription> subscribers) {
        if (subscribers != null) {
            subscribers.parallelStream()
                    .forEach(sub -> sub.notify(message));
        }
    }
}

package fr.sorbonne_u.components.pubsub.components;

import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;

/**
 * A {@code PubSubNode} component acts like a <b>local</b> {@code PubSub} component for each host,
 * and should only used in Distributed CVM.
 * <p>
 * The {@code PubSubNode} is not just a {@code PubSub}, it's also a {@code Publisher} and {@code Subscriber}
 * at the same time.
 * <p>
 * By doing so, when a {@code PubSubNode} component is instantiated, it will subscribe to the in-bound port
 * of a unique {@code PubSub} component (this component is unique among all the hosts),
 * the {@code PubSubNode} {@code Publisher} component also will establish the connection
 * between his out-bound port and the in-bound port of the  unique {@code PubSub} component.
 * <p>
 * When regular {@code Publisher} components and {@code Subscriber} components are created, they must establish
 * a connection between their out-bound ports and this {@code PubSubNode} component in-bound port,
 * so when a message will be published, it will pass by this {@code PubSubNode} component,
 * the {@code PubSubNode} then will not publish the message to its subscribers,
 * but it will forward the message the unique {@code PubSub} component,
 * this unique {@code PubSub} component then will publish the message to all its subscribers ({@code PubSubNode}s),
 * and so when the {@code PubSubNode} receive the message then, and only then it will publish it to its
 * regular subscriber components
 * <p>
 * This architecture allow us to build a tree of a {@code PubSubNode} instances whom
 * root is a one unique {@code PubSub} component, which will maximize the concurrency when there will be
 * a significant number of machines or {@code Publisher} , {@code Subscriber} components.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see PubSub
 */
public class PubSubNode extends PubSub {
    private static PubSubNode common;
    private Publisher publisher;
    private Subscriber subscriber;


    private PubSubNode(Builder builder) throws Exception {
        super(builder);
        subscriber = builder.subscriber;
        publisher = builder.publisher;
        subscriber.setMessageConsumer(this::tryPublish);
    }

    /**
     * Creates a new common PubSub.
     *
     * @param dcvm                 An {@code AbstractDistributedCVM}
     * @param pubSubInBoundPortURI The unique {@code PubSub} component in-bound port URI
     *                             that the {@code PubSubNode} will connect to
     * @see PubSub#newCommonPubSub
     */
    protected static void newCommonPubSubNode(AbstractDistributedCVM dcvm, String pubSubInBoundPortURI) throws Exception {
        if (common == null) {
            common = newBuilder(dcvm, pubSubInBoundPortURI).build();
        }
    }

    /**
     * get the common {@code PubSubNode} component.
     *
     * @return the common {@code PubSub} component
     * @throws IllegalStateException if the common {@code PubSubNode} component is not initialized.
     */
    protected static PubSub getCommonPubSubNode() {
        if (common == null)
            throw new IllegalStateException("CommonPubSubNode need to be initialized first.");
        return common;
    }

    /**
     * Creates a new {@code PubSubNode} {@code Builder}.
     *
     * @param cvm                  An {@code AbstractCVM}
     * @param pubSubInBoundPortURI The unique {@code PubSub} component in-bound port URI
     *                             that the {@code PubSubNode} will connect to
     * @return A {@code PubSubNode} builder
     */
    public static Builder newBuilder(AbstractCVM cvm, String pubSubInBoundPortURI) throws Exception {
        Publisher publisher = Publisher
                .newBuilder(cvm, pubSubInBoundPortURI)
                .setTracing(false)
                .setLogging(false)
                .build();

        Subscriber subscriber = Subscriber
                .newBuilder(cvm, pubSubInBoundPortURI)
                .setTracing(false)
                .setLogging(false)
                .build();

        return new Builder(cvm, publisher, subscriber);
    }

    /**
     * Publish the message to the unique {@code PubSub} component.
     *
     * @param message A {@code Message}
     */
    @Override
    public void publish(Message message) {
        this.publisher.publish(message);
    }

    /**
     * When a message is received from the unique {@code PubSub} component,
     * the message will be published to the actual subscribers of this {@code PubSubNode} component.
     *
     * @param message A {@code Message}
     */
    private void tryPublish(Message message) {
        try {
            super.publish(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * The {@code Builder} class of this {@code PubSubNode} component.
     *
     * @see CBuilder
     */
    public static class Builder extends PubSub.Builder {
        private final Subscriber subscriber;
        private final Publisher publisher;

        /**
         * Creates a new {@code PubSubNode} builder.
         *
         * @param cvm        An {@code AbstractCVM}
         * @param publisher  A {@code Publisher} that will publish messages to the unique {@code PubSub} component
         * @param subscriber A {@code Subscriber} that will receive messages from the unique {@code PubSub} component
         */
        private Builder(AbstractCVM cvm, Publisher publisher, Subscriber subscriber) {
            super(cvm);
            this.subscriber = subscriber;
            this.publisher = publisher;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public PubSubNode build() throws Exception {
            PubSubNode pubSubNode = new PubSubNode(this);
            deploy(pubSubNode);
            pubSubNode.subscriber.subscribe(Topic.ROOT);
            return pubSubNode;
        }
    }

}

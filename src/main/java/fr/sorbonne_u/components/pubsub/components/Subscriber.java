package fr.sorbonne_u.components.pubsub.components;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;
import fr.sorbonne_u.components.pubsub.connectors.SubscriberServiceConnector;
import fr.sorbonne_u.components.pubsub.interfaces.Observer;
import fr.sorbonne_u.components.pubsub.interfaces.SubscriberService;
import fr.sorbonne_u.components.pubsub.interfaces.SubscriberService.Required;
import fr.sorbonne_u.components.pubsub.port.SubscriberInBoundPort;
import fr.sorbonne_u.components.pubsub.port.SubscriberOutBoundPort;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static fr.sorbonne_u.components.ports.AbstractPort.generatePortURI;

/**
 * A {@code Subscriber} component handles subscriber related requests and send them to the PubSub.
 * The {@code Subscriber} implement all the methods of the two
 * {@code SubscriberService}, {@code Observer} interfaces methods and delegates their implementations
 * to its {@code SubscriberOutBoundPort} and its {@code SubscriberInBoundPort}.
 * <p>
 * This component has a required interface {@link fr.sorbonne_u.components.pubsub.interfaces.SubscriberService.Required}
 * and an offered interface {@link fr.sorbonne_u.components.pubsub.interfaces.Observer.Offered} so that the component can receive messages.
 * <p>
 * The connection between this component {@code SubscriberOutBoundPort} and the {@code PubSub}
 * component in-bound port is established by using the {@link SubscriberServiceConnector} class.
 * <p>
 * The connection between this  component {@code SubscriberInBoundPort} and the {@code PubSub}
 * component out-bound port is established by using the
 * {@link fr.sorbonne_u.components.pubsub.connectors.ObserverConnector} class.
 * <p>
 * When A subscriber receive a message, it will execute a {@code Consumer} of a {@code Message},
 * note that the order of messages that this component will receive is not specified.
 * <p>
 * A {@code Subscriber} ID is his in-bound port URI.
 * <p>
 * In a CVM, this component need to establish the connection between his out-bound port and a {@code PubSub} component,
 * however, in a Distributed CVM, this component need to establish the connection between his out-bound port
 * and a {@code PubSubNode} component in-bound port.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see Builder
 * @see SubscriberService
 * @see Observer
 * @see SubscriberOutBoundPort
 * @see SubscriberInBoundPort
 * @see SubscriberServiceConnector
 * @see fr.sorbonne_u.components.pubsub.connectors.ObserverConnector
 */
@RequiredInterfaces(required = Required.class)
@OfferedInterfaces(offered = Observer.Offered.class)
public class Subscriber extends AbstractComponent implements SubscriberService, Observer {
    /**
     * This component out-bound port.
     */
    private final SubscriberOutBoundPort subscriberOutBoundPort;

    /**
     * This component in-bound port.
     */
    private final SubscriberInBoundPort subscriberInBoundPort;
    /**
     * A block of code which is executed each time a message is received.
     */
    private Consumer<Message> block;

    /**
     * Creates a new Subscriber
     *
     * @param builder A {@code Builder}
     */
    private Subscriber(Builder builder) throws Exception {
        // call the super constructor, if reflectionInboundPortURI is null then generate one.
        super(builder.reflectionInboundPortURI == null ?
                builder.reflectionInboundPortURI = generatePortURI(ReflectionI.class) :
                builder.reflectionInboundPortURI, builder.nbThreads, builder.nbSchedulableThreads);

        this.block = builder.block;

        // initialize the subscriberInBoundPor
        this.subscriberInBoundPort = builder.subscriberInBoundPortURI == null ?
                new SubscriberInBoundPort(this) :
                new SubscriberInBoundPort(builder.subscriberInBoundPortURI, this);

        // initialize the subscriberOutBoundPort
        this.subscriberOutBoundPort = builder.subscriberOutBoundPortURI == null ?
                new SubscriberOutBoundPort(this, this.subscriberInBoundPort.getPortURI()) :
                new SubscriberOutBoundPort(builder.subscriberOutBoundPortURI, this, this.subscriberInBoundPort.getPortURI());

        // publishing
        this.addPort(this.subscriberInBoundPort);
        this.subscriberInBoundPort.publishPort();

        // publishing
        this.addPort(this.subscriberOutBoundPort);
        this.subscriberOutBoundPort.localPublishPort();


        this.tracer.setTitle(builder.reflectionInboundPortURI);
        this.tracer.setRelativePosition(1, 1);

    }

    /**
     * Creates a new {@code Subscriber} builder, this {@code Subscriber} component will
     * be deployed and configured to the specified cvm and will be connected to the a {@code PubSub}
     * component via the {@code pubSubInBoundPortURI} URI.
     *
     * @param cvm                  A CVM
     * @param pubSubInBoundPortURI The {@code PubSub} in-bound port URI that this {@code Publisher}
     *                             will connects to
     * @return a new {@code Publisher} builder
     */
    public static Builder newBuilder(AbstractCVM cvm, String pubSubInBoundPortURI) throws Exception {
        return new Builder(cvm, pubSubInBoundPortURI);
    }

    /**
     * Creates a new {@code Publisher} builder, this {@code Publisher} component will
     * be deployed and configured to the specified cvm and will be connected to the {@code Common PubSub}
     *
     * @param cvm A CVM
     * @return a new {@code Publisher} builder
     */
    public static Builder newBuilder(AbstractCVM cvm) throws Exception {
        return newBuilder(cvm, PubSub.getCommonPubSub().getInBoundPortURI());
    }

    /**
     * Creates a new {@code Publisher} builder, this {@code Publisher} component will
     * be deployed and configured to the specified cvm and will be connected to the {@code Common PubSubNode}
     *
     * @param dcvm A Distributed CVM
     * @return a new {@code Publisher} builder
     * @see PubSubNode
     * @see AbstractDistributedCVM
     */
    public static Builder newBuilder(AbstractDistributedCVM dcvm) throws Exception {
        return newBuilder(dcvm, PubSubNode.getCommonPubSubNode().getInBoundPortURI());
    }

    /**
     * @see AbstractComponent#finalise()
     */
    @Override
    public void finalise() throws Exception {
        this.logMessage("stopping component.");
        this.subscriberOutBoundPort.doDisconnection();
        this.subscriberOutBoundPort.unpublishPort();
        super.finalise();
    }

    /**
     * @see AbstractComponent#shutdown()
     */
    @Override
    public void shutdown() throws ComponentShutdownException {
        try {
            subscriberInBoundPort.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

    /**
     * @see AbstractComponent#shutdownNow()
     */
    @Override
    public void shutdownNow() throws ComponentShutdownException {
        try {
            subscriberInBoundPort.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdownNow();
    }

    @Override
    public void subscribe(Topic topic) {
        this.logMessage("subscriber subscribe to a topic: " + topic);
        this.subscriberOutBoundPort.subscribe(topic);
    }

    @Override
    public void subscribe(Topic topic, Predicate<Message> filter) {
        this.logMessage("subscribing to a topic by a filter: " + topic);
        this.subscriberOutBoundPort.subscribe(topic, filter);
    }

    @Override
    public void filter(Topic topic, Predicate<Message> filter) {
        this.logMessage("filtering a topic: " + topic);
        this.subscriberOutBoundPort.filter(topic, filter);

    }

    @Override
    public void unsubscribe(Topic topic) {
        this.logMessage("un-subscribing from a topic: " + topic);
        this.subscriberOutBoundPort.unsubscribe(topic);

    }

    @Override
    public void unsubscribe() {
        this.logMessage("un-subscribing from all topics.");
        this.subscriberOutBoundPort.unsubscribe();

    }

    @Override
    public void notify(Message message) {
        this.logMessage("receiving message : " + message.getContent());
        if (block != null)
            block.accept(message);

    }


    @Override
    public String getSubId() {

        try {
            return subscriberInBoundPort.getPortURI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Set block of code which will be executed each time a message is received.
     *
     * @param block A {@code Message} consumer
     */
    public void setMessageConsumer(Consumer<Message> block) {
        this.block = block;
    }

    /**
     * Do a port connection between this component subscriberOutBoundPort and a
     * {@code PubSub} in-bound port URI.
     *
     * @param uri A {@code PubSub} in-bound port URI.
     */
    private void doPortConnection(String uri) throws Exception {
        this.doPortConnection(
                subscriberOutBoundPort.getPortURI(),
                uri,
                SubscriberServiceConnector.class.getCanonicalName());
    }

    /**
     * The {@code Builder} class of this {@code Subscriber } component.
     *
     * @see CBuilder
     */
    public static class Builder extends CBuilder<Builder> {
        private final String pubSubInBoundPortURI;
        private String subscriberOutBoundPortURI;
        private String subscriberInBoundPortURI;
        private Consumer<Message> block;

        private Builder(AbstractCVM cvm, String pubSubInBoundPortURI) {
            super(cvm);
            nbThreads = 1;
            nbSchedulableThreads = 1;
            this.pubSubInBoundPortURI = pubSubInBoundPortURI;
        }

        /**
         * Set the out-bound port URI of this{@code Subscriber} component.
         *
         * @param subscriberOutBoundPortURI This component out-bound port URI
         * @return This builder
         */
        public Builder setSubscriberOutBoundPortURI(String subscriberOutBoundPortURI) {
            this.subscriberOutBoundPortURI = subscriberOutBoundPortURI;
            return this;
        }

        /**
         * Set block of code which will be executed each time a message is received.
         *
         * @param block A {@code Message} consumer
         * @return This builder
         */
        public Builder setMessageConsumer(Consumer<Message> block) {
            this.block = block;
            return this;
        }

        /**
         * Set the in-bound port URI of this{@code Subscriber} component.
         *
         * @param subscriberInBoundPortURI This component in-bound port URI
         * @return This builder
         */
        public Builder setSubscriberInBoundPortURI(String subscriberInBoundPortURI) {
            this.subscriberInBoundPortURI = subscriberInBoundPortURI;
            return this;

        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Subscriber build() throws Exception {
            Subscriber subscriber = new Subscriber(this);
            deploy(subscriber);
            subscriber.doPortConnection(pubSubInBoundPortURI);
            return subscriber;
        }
    }


}

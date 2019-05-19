package fr.sorbonne_u.components.pubsub.components;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;
import fr.sorbonne_u.components.pubsub.connectors.ObserverConnector;
import fr.sorbonne_u.components.pubsub.exceptions.UnPublishPortException;
import fr.sorbonne_u.components.pubsub.interfaces.Observer;
import fr.sorbonne_u.components.pubsub.interfaces.PubSubService;
import fr.sorbonne_u.components.pubsub.interfaces.Subscription;
import fr.sorbonne_u.components.pubsub.port.PubSubInBoundPort;
import fr.sorbonne_u.components.pubsub.port.PubSubOutBoundPort;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static fr.sorbonne_u.components.ports.AbstractPort.generatePortURI;

/**
 * A {@code PubSub}  is the heart of the publication/subscription system.
 * <p>
 * Its main role is to route requests from Publishers and Subscribers to its {@link Broker},
 * It  implements all the methods of the {@code PubSubService} interface
 * and delegates their implementations to its {@code Broker}.
 * The component offers {@link fr.sorbonne_u.components.pubsub.interfaces.PubSubService.Offered} interface.
 * <p>
 * <p>
 * The {@code PubSub}  is also responsible of creating {@code Subscription} instances
 * for each subscription, each {@code Subscription} instance will have a {@code PubSubOutBoundPort}
 * instance which is connected to a {@code SubscriberInBoundPort}
 * so it can notify the subscriber with a message,
 * thus the {@code PubSub} component requires a {@link Observer.Required} interace.
 * <p>
 * Using the {@code PubSub} in a CVM:
 * The {@code PubSub}  offers a <b><i>common</i></b> {@code PubSub} instance with a default configuration,
 * users of this component can initialize it by calling the {@link #newCommonPubSub(AbstractCVM)} method,
 * the class also offers builder methods to manually instantiate and configure the {@code PubSub} component.
 * Example of using the common {@code PubSub} in a CVM:
 * <blockquote><pre>
 *      PubSub.newCommonPubSub(someCVM);
 *
 *      publisher = Publisher.newBuilder(someCVM).build();
 *      subscriber = Subscriber.newBuilder(someCVM).build();
 *      subscriber.subscribe(someTopic);
 *
 *      Message msg = Message.newBuilder(someTopic)
 *          .setContent("Hello from publisher")
 *          .build();
 *
 *      publisher.publish(msg);
 * </pre></blockquote>
 * <p>
 * Example of using a new configured {@code PubSub} in a CVM:
 * <blockquote><pre>
 *      PubSub pubSub = PubSub.newBuilder(someCVM)
 *          .setPubSubInBoundPortURI(someURI)
 *          .setPublishingParallelism(5)
 *          .setSubscribingParallelism(5)
 *          .build();
 *
 *      publisher = Publisher.newBuilder(someCVM,someURI).build();
 *      subscriber = Subscriber.newBuilder(someCVM,someURI).build();
 *      subscriber.subscribe(someTopic);
 *
 *      Message msg = Message.newBuilder(someTopic)
 *          .setContent("Hello from publisher")
 *          .build();
 *
 *      publisher.publish(msg);
 *  </pre></blockquote>
 * <p>
 * Using the {@code PubSub} in a Distributed CVM:
 * <p>
 * The common {@code PubSub} component can be used also for a Distributed CVM,
 * however bear in mind that in a Distributed CVM only one instance of a  {@code PubSub} component
 * among all the hosts is required, and a {@code PubSubNode} instance for each host,
 * each {@code PubSubNode} must connect to this one unique {@code PubSub} component,
 * hence, all the  {@code PubSub} component clients are basically {@code PubSubNode} components.
 * <p>
 * Note that the {@code Publisher} components and the {@code Subscriber} components will use the
 * {@code PubSubNode} in-bound port to establish the connection to their
 * out-bound ports, bear in mind that they use the {@code PubSubNode} in-bound port and
 * <b>NOT</b> the {@code PubSub} in-bound port to establish the connection,
 * since the {@code PubSubNode} is already connected to the {@code PubSub}in-bound port.
 *
 * <p>
 * Example of using the common {@code PubSub} in Distributed a CVM:
 * <blockquote><pre>
 *      // in the DistributedCVM instantiateAndPublish method
 *      public void instantiateAndPublish() throws Exception {
 *          PubSub.newCommonPubSub(this, thisJVMURI.equals(someHost));
 *          if (thisJVMURI.equals(someHost)) {
 *
 *              publisher = Publisher.newBuilder(this).build();
 *
 *          } else if (thisJVMURI.equals(someOtherHost)) {
 *
 *              subscriber = Subscriber.newBuilder(this).build();
 *         }
 *
 *         super.instantiateAndPublish();
 *      }
 *
 *      public void start() throws Exception {
 *          if (thisJVMURI.equals(someHost)) {
 *              Message msg = Message.newBuilder(someTopic)
 *                  .setContent("Hello")
 *                  .build();
 *
 *              publisher.publish(msg);
 *         } else if (thisJVMURI.equals(someOtherHost)) {
 *
 *              subscriber.subscribe(someTopic);
 *
 *         }
 *          super.start();
 *      }
 *  </pre></blockquote>
 * Example of using a new configured {@code PubSub} in a Distributed CVM:
 * <blockquote><pre>
 *      // in the DistributedCVM instantiateAndPublish method
 *      public void instantiateAndPublish() throws Exception {
 *          if (thisJVMURI.equals(someHost)) {
 *              // create only one PubSub component with in-port URI = someURI
 *              PubSub pubSub = PubSub.newBuilder(this)
 *                      .setPubSubInBoundPortURI(someURI)
 *                      .build();
 *
 *              pubSubNode = PubSubNode.newBuilder(this, someURI).build();
 *
 *              // the publisher now connects to this PubSubNode, if you don't specify
 *              // its in-bound URI then an NullPointerException will be thrown since
 *              // it will try to connect to the common PubSubNode which is not initialized at all.
 *              publisher = Publisher.newBuilder(this,pubSubNode.getInBoundPortURI())
 *                      .build();
 *
 *          } else if (thisJVMURI.equals(someOtherHost)) {
 *              // do NOT create an other PubSub component
 *              // use the PubSub component of the "someHost" machine in-bound port for this PubSubNode
 *              pubSubNode = PubSubNode.newBuilder(this, someURI).build();
 *              subscriber = Subscriber.newBuilder(this,pubSubNode.getInBoundPortURI())
 *                      .build();
 *         }
 *
 *         super.instantiateAndPublish();
 *      }
 *
 *      public void start() throws Exception {
 *          if (thisJVMURI.equals(someHost)) {
 *              Message msg = Message.newBuilder(someTopic)
 *                  .setContent("Hello")
 *                  .build();
 *
 *              publisher.publish(msg);
 *         } else if (thisJVMURI.equals(someOtherHost)) {
 *
 *              subscriber.subscribe(someTopic);
 *
 *         }
 *          super.start();
 *      }
 *  </pre></blockquote>
 * <p>
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see PubSubNode
 * @see PubSubService
 * @see Observer
 * @see PubSubInBoundPort
 * @see PubSubOutBoundPort
 * @see Broker
 * @see Subscription
 */
@OfferedInterfaces(offered = {PubSubService.Offered.class})
@RequiredInterfaces(required = {Observer.Required.class})

public class PubSub extends AbstractComponent implements PubSubService {
    private final static String COMMON_PUBSUB = "COMMON_PUBSUB";
    /**
     * A common PubSub instance
     */
    private static PubSub common;

    /**
     * The {@code PubSub} broker.
     */
    private final Broker broker;

    /**
     * The {@code PubSub} in-bound port
     */
    private PubSubInBoundPort pubSubInBoundPort;

    /**
     * Creates a new {@code} PubSub component using its builder.
     *
     * @param builder A {@code PubSub} builder.
     */
    protected PubSub(Builder builder) throws Exception {
        super(builder.reflectionInboundPortURI == null ?
                builder.reflectionInboundPortURI = generatePortURI() :
                builder.reflectionInboundPortURI, builder.nbThreads, builder.nbSchedulableThreads);

        broker = new Broker(builder.subscribingParallelism, builder.publishingParallelism);
        this.pubSubInBoundPort = builder.pubSubInBoundPortURI == null ?
                new PubSubInBoundPort(this) :
                new PubSubInBoundPort(builder.pubSubInBoundPortURI, this);

        this.addPort(pubSubInBoundPort);
        pubSubInBoundPort.publishPort();

        this.tracer.setTitle(builder.reflectionInboundPortURI);
        this.tracer.setRelativePosition(1, 0);
    }

    /**
     * Initialize the common {@code PubSub} and deploy it to a CVM.
     * {@code Publisher} components and {@code Subscriber} components will use this
     * common {@code PubSub} in-bound port to establish the connection to their
     * out-bound ports  in a default configuration.
     *
     * @param cvm An {@code AbstractCVM} instance
     */
    public static void newCommonPubSub(AbstractCVM cvm) throws Exception {
        Objects.requireNonNull(cvm);
        if (common == null) {
            common = PubSub.newBuilder(cvm).build();
        }
    }

    /**
     * Initialize the common {@code PubSub} component with an in-bound port URI equals to "COMMON_PUBSUB"
     * and deploy it to the Distributed CVM.
     * <p>
     * Initialize also the common {@code PubSubNode} component, connect
     * it to this common {@code PubSub} in-boun port URI "COMMON_PUBSUB" and deploy it the Distributed CVM
     * .
     * <p>
     * Example:
     * <blockquote><pre>
     *      PubSub.newCommonPubSub(someDCVM, someDCVM.thisJVMURI.equals(myHost));
     * </pre></blockquote>
     *
     * @param dcvm   An {@code AbstractDistributedCVM}
     * @param isRoot If true then initialize the common {@code PubSub} and the common {@code PubSubNode}
     *               else initialize just the common {@code PubSubNode}
     * @see #newCommonPubSub(AbstractDistributedCVM, boolean, String)
     */
    public static void newCommonPubSub(AbstractDistributedCVM dcvm, boolean isRoot) throws Exception {
        newCommonPubSub(dcvm, isRoot, COMMON_PUBSUB);
    }

    /**
     * Initialize the common  {@code PubSub} component with a specific in-bound port URI
     * and deploy it to a Distributed CVM.
     * <p>
     * <p>
     * In a Distributed CVM, only one {PubSub} component is required among all the hosts,
     * however for each host, a common {@code PubSubNode} component will be deployed  to the Distributed CVM
     * and connected to this common {@code PubSub} component,
     * so make sure to set {@code isRoot} to true only in one host.
     * Example:
     *
     * <blockquote><pre>
     *        PubSub.newCommonPubSub(someDCVM, someDCVM.thisJVMURI.equals(myHost),someURI);
     * </pre></blockquote>
     * Note that the {@code Publisher} components and the {@code Subscriber} components will use the
     * common {@code PubSubNode} in-bound port to establish the connection to their
     * out-bound ports, bear in mind that they use the common {@code PubSubNode} in-bound port and
     * <b>NOT</b> the common{@code PubSub} in-bound port to establish the connection,
     * since the common {@code PubSubNode} is already connected to the common {@code PubSub}in-bound port.
     *
     * @param dcvm                 An {@code AbstractDistributedCVM}
     * @param isRoot               If true then initialize the common {@code PubSub} and the common {@code PubSubNode}
     *                             else initialize just the common {@code PubSubNode}
     * @param pubSubInBoundPortURI The common {@code PubSub} component in-bound port URI.
     */
    public static void newCommonPubSub(AbstractDistributedCVM dcvm, boolean isRoot, String pubSubInBoundPortURI) throws Exception {
        Objects.requireNonNull(dcvm);
        Objects.requireNonNull(pubSubInBoundPortURI);
        if (isRoot && common == null) {
            common = PubSub.newBuilder(dcvm)
                    .setPubSubInBoundPortURI(pubSubInBoundPortURI)
                    .build();
        }

        PubSubNode.newCommonPubSubNode(dcvm, pubSubInBoundPortURI);
    }

    /**
     * get the common {@code PubSub} component.
     *
     * @return the common {@code PubSub} component
     * @throws IllegalStateException if the common {@code PubSub} component is not initialized.
     */
    public static PubSub getCommonPubSub() {
        if (common == null)
            throw new IllegalStateException("CommonPubSub need to be initialized first.");
        return common;
    }

    /**
     * Creates a new {@code PubSub} {@code Builder}.
     *
     * @param cvm An {@code AbstractCVM}
     * @return A {@code PubSub} builder
     */
    public static Builder newBuilder(AbstractCVM cvm) {
        return new Builder(cvm);
    }


    @Override
    public void finalise() throws Exception {
        this.logMessage("stopping  component.");
        broker.shutdown();
        super.finalise();

    }

    @Override
    public void shutdown() throws ComponentShutdownException {
        unPublishPort();
        super.shutdown();
    }

    @Override
    public void shutdownNow() throws ComponentShutdownException {
        unPublishPort();
        super.shutdownNow();
    }

    private void unPublishPort() throws ComponentShutdownException {
        try {
            pubSubInBoundPort.unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
    }


    @Override
    public void publish(Message message) {
        this.logMessage("publishing: " + message.getContent() + " to " + message.getTopic());
        broker.publish(message);
    }

    @Override
    public void subscribe(String subId, Topic topic) throws Exception {
        this.logMessage("subscribing " + subId + " to :" + topic);

        this.broker.subscribe(new SubscriptionImpl(this, subId), topic);
    }

    @Override
    public void subscribe(String subId, Topic topic, Predicate<Message> filter) throws Exception {
        this.logMessage("subscribing with filter " + subId + " to :" + topic);
        this.broker.subscribe(new SubscriptionImpl(this, subId), topic, filter);

    }


    @Override
    public void filter(String subId, Topic topic, Predicate<Message> filter) {
        this.logMessage(subId + " filtering: " + topic);
        this.broker.filter(subId, topic, filter);
    }

    @Override
    public void unsubscribe(String subId, Topic topic) {
        this.logMessage("unsubscribing " + subId + " subscription from :" + topic);
        this.broker.unsubscribe(subId, topic);
    }

    @Override
    public void unsubscribe(String subId) {
        this.logMessage("removing " + subId + " subscription from all topics");
        broker.unsubscribe(subId);
    }

    /**
     * Check if a topic exists.
     *
     * @param topic A {@code Topic}
     * @return true if the topic exists.
     */
    public boolean hasTopic(Topic topic) {
        return broker.hasTopic(topic);
    }

    /**
     * Delete a topic.
     * <p>
     * When deleting a topic it is not guaranteed that all the subscribers to this topic will get
     * the messages published on this topic at this moment.
     * </p>
     *
     * @param topic A {@code Topic}
     */
    public void removeTopic(Topic topic) {
        broker.removeTopic(topic);
    }

    /**
     * Get all topics.
     *
     * @return a set of topics
     */
    public Set<Topic> getTopics() {
        return broker.geTopics();
    }

    /**
     * Get the in-bound port of this {@code PubSub} component.
     *
     * @return the in-bound port of this {@code PubSub}  component
     */
    public String getInBoundPortURI() throws Exception {
        return pubSubInBoundPort.getPortURI();
    }

    /**
     * The {@code Builder} class of this {@code PubSub } component.
     *
     * @see CBuilder
     */
    public static class Builder extends CBuilder<Builder> {

        private int subscribingParallelism;
        private int publishingParallelism;
        private String pubSubInBoundPortURI;

        protected Builder(AbstractCVM cvm) {
            super(cvm);
            nbThreads = 10;
            nbSchedulableThreads = 0;
            subscribingParallelism = 10;
            publishingParallelism = 10;
        }

        /**
         * Set the subscribing parallelism for the broker of this PubSub component.
         *
         * @param subscribingParallelism An {@code int} value
         * @return This builder
         * @throws IllegalArgumentException if {@code subscribingParallelism} is <= 0
         */
        public Builder setSubscribingParallelism(int subscribingParallelism) {
            if (subscribingParallelism <= 0)
                throw new IllegalArgumentException("subscribingParallelism must be > 0");

            this.subscribingParallelism = subscribingParallelism;
            return this;
        }

        /**
         * Set the publishing parallelism for the broker of this PubSub component.
         *
         * @param publishingParallelism An {@code int} value
         * @return This builder
         * @throws IllegalArgumentException if {@code publishingParallelism} is <= 0
         */
        public Builder setPublishingParallelism(int publishingParallelism) {
            if (publishingParallelism <= 0)
                throw new IllegalArgumentException("publishingParallelism must be > 0");
            this.publishingParallelism = publishingParallelism;
            return this;
        }

        /**
         * Set the in-bound port URI of this PubSub component.
         *
         * @param pubSubInBoundPortURI A unique {@code String}
         * @return This builder
         */
        public Builder setPubSubInBoundPortURI(String pubSubInBoundPortURI) {
            this.pubSubInBoundPortURI = pubSubInBoundPortURI;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public PubSub build() throws Exception {
            PubSub pubSub = new PubSub(this);
            deploy(pubSub);
            return pubSub;
        }
    }

    /**
     * A {@code SubscriptionImpl} is a class that holds all related information
     * about a {@link fr.sorbonne_u.components.pubsub.components.Subscriber} component, mainly its ID,
     * a {@code PubSubOutBoundPort} to notify it and a filter
     * <p>
     * When a Subscriber component subscribe to some topic, an instance of this class is created by the
     * {@code PubSub} and routed to the broker, when instantiated this class uses
     * the Subscriber ID to establish the connection between the {@code PubSubOutBoundPort} and
     * the {@code SubscriberInBoundPort}.
     * <p>
     * Since a {@code Subscription} instance is manipulated by multiple threads, it must carefully
     * synchronize its operations, mainly in this scenario:
     * when a {@code end} is called, the PubSubOutBoundPort is unpublished and cannot be used,
     * in the same time the {@link PublisherExecutor} executor
     * is calling the {@link #notify} method.
     * if the two methods {@link #notify} and {@link #end} are not synchronized then an exception
     * will occur since the port would be already unpublished.
     *
     * @see Broker
     */
    private static class SubscriptionImpl implements Subscription {

        private final PubSubOutBoundPort pubSubOutBoundPort;
        private final String subscriberInBoundPortURI;
        private Predicate<Message> filter;


        private SubscriptionImpl(PubSub owner, String subscriberInBoundPortURI) throws Exception {
            this.pubSubOutBoundPort = new PubSubOutBoundPort(owner);
            owner.addPort(pubSubOutBoundPort);
            this.pubSubOutBoundPort.localPublishPort();
            this.subscriberInBoundPortURI = subscriberInBoundPortURI;

            owner.doPortConnection(
                    pubSubOutBoundPort.getPortURI(),
                    subscriberInBoundPortURI,
                    ObserverConnector.class.getCanonicalName());

        }

        /**
         * See if there is a filter for this message.
         *
         * @param message A {@code Message}
         * @return true if the filter is accepted
         */
        private boolean accept(Message message) {
            if (filter == null)
                return true;

            return filter.test(message);
        }

        @Override
        public void notify(Message message) {
            synchronized (pubSubOutBoundPort) {
                try {
                    if (accept(message))
                        this.pubSubOutBoundPort.notify(message);
                } catch (Exception e) {
                    end();
                }
            }

        }

        @Override
        public void filter(Predicate<Message> filter) {
            this.filter = filter;
        }

        @Override
        public void end() {
            synchronized (pubSubOutBoundPort) {
                try {
                    this.pubSubOutBoundPort.doDisconnection();
                    this.pubSubOutBoundPort.unpublishPort();
                } catch (Exception e) {
                    throw new UnPublishPortException(e);
                }
            }

        }


        @Override
        public String getSubId() {
            return subscriberInBoundPortURI;
        }

    }

}

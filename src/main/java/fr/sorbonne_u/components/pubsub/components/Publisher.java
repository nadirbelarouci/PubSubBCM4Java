package fr.sorbonne_u.components.pubsub.components;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.cvm.AbstractDistributedCVM;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.connectors.PublisherServiceConnector;
import fr.sorbonne_u.components.pubsub.interfaces.PublisherService;
import fr.sorbonne_u.components.pubsub.port.PublisherOutBoundPort;

import static fr.sorbonne_u.components.ports.AbstractPort.generatePortURI;

/**
 * A {@code Publisher} component handles publishing messages to the PubSub.
 * The {@code Publisher} implement all the methods of the {@code PublisherService} interface methods
 * and delegates their implementations to its {@code PublisherOutBoundPort}.
 * This component has only one required interface: {@link PublisherService.Required}
 * <p>
 * In a CVM, this component need to establish the connection between his out-bound port and a {@code PubSub} component,
 * however, in a Distributed CVM, this component need to establish the connection between his out-bound port
 * and a {@code PubSubNode} component in-bound port.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see PublisherService
 * @see PublisherOutBoundPort
 * @see PublisherServiceConnector
 */
@RequiredInterfaces(required = PublisherService.Required.class)
public class Publisher extends AbstractComponent implements PublisherService {
    /**
     * The component out-bound port.
     */
    private PublisherOutBoundPort publisherOutBoundPort;

    /**
     * Creates a new publisher using its builder.
     * Note that in order to create an instance of this class you must use its builders.
     *
     * @param builder A {@code Builder}
     * @throws Exception An exception while creating the component
     * @see #newBuilder(AbstractCVM)
     * @see #newBuilder(AbstractCVM, String)
     * @see #newBuilder(AbstractDistributedCVM)
     */
    private Publisher(Builder builder) throws Exception {
        super(builder.reflectionInboundPortURI == null ?
                builder.reflectionInboundPortURI = generatePortURI() :
                builder.reflectionInboundPortURI, builder.nbThreads, builder.nbSchedulableThreads);

        this.publisherOutBoundPort = publisherOutBoundPort == null ?
                new PublisherOutBoundPort(this) :
                new PublisherOutBoundPort(builder.publisherOutBoundPortURI, this);

        this.addPort(this.publisherOutBoundPort);
        this.publisherOutBoundPort.localPublishPort();

        this.tracer.setTitle(builder.reflectionInboundPortURI);
        this.tracer.setRelativePosition(1, 1);
    }

    /**
     * Creates a new {@code Publisher} builder, this {@code Publisher} component will
     * be deployed and configured to the specified cvm and will be connected to the a {@code PubSub}
     * component via the {@code pubSubInBoundPortURI} URI.
     *
     * @param cvm                  A CVM
     * @param pubSubInBoundPortURI The {@code PubSub} in-bound port URI that this {@code Publisher}
     *                             will connects to
     * @return a new {@code Publisher} builder
     */
    public static Builder newBuilder(AbstractCVM cvm, String pubSubInBoundPortURI) {
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
     * @see fr.sorbonne_u.components.AbstractComponent#finalise()
     */
    @Override
    public void finalise() throws Exception {
        this.logMessage("stopping  component.");
        this.publisherOutBoundPort.doDisconnection();
        this.publisherOutBoundPort.unpublishPort();
        super.finalise();
    }

    @Override
    public void publish(Message message) {
        this.logMessage("publishing: " + message.getContent() + " -> " + message.getTopic() + ".");
        this.publisherOutBoundPort.publish(message);
    }

    /**
     * Do a port connection between this component pubSubInBoundPortURI and a {@code PubSub} in-bound port URI.
     *
     * @param uri The {@code PubSub} in-bound port URI.
     */
    private void doPortConnection(String uri) throws Exception {
        super.doPortConnection(
                publisherOutBoundPort.getPortURI(),
                uri,
                PublisherServiceConnector.class.getCanonicalName());
    }

    /**
     * The {@code Builder} class of this {@code Publisher } component.
     *
     * @see CBuilder
     */
    public static class Builder extends CBuilder<Builder> {
        private final String pubSubInBoundPortURI;
        private String publisherOutBoundPortURI;

        private Builder(AbstractCVM cvm, String pubSubInBoundPortURI) {
            super(cvm);
            nbThreads = 1;
            nbSchedulableThreads = 1;
            this.pubSubInBoundPortURI = pubSubInBoundPortURI;

        }

        /**
         * Set the out-bound port URI of this{@code Publisher} component.
         *
         * @param publisherOutBoundPortURI This component out-bound port URI
         * @return This builder
         */
        public Builder setPublisherOutBoundPortURI(String publisherOutBoundPortURI) {
            this.publisherOutBoundPortURI = publisherOutBoundPortURI;
            return this;
        }


        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Publisher build() throws Exception {
            Publisher publisher = new Publisher(this);
            deploy(publisher);
            publisher.doPortConnection(pubSubInBoundPortURI);
            return publisher;
        }
    }


}

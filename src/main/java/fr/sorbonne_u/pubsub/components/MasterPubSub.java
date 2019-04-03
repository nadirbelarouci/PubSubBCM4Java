package fr.sorbonne_u.pubsub.components;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.connectors.MessagePublisherServiceConnector;
import fr.sorbonne_u.pubsub.interfaces.*;
import fr.sorbonne_u.pubsub.port.MessagePublisherOutBoundPort;
import fr.sorbonne_u.pubsub.port.PubSubInBoundPort;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@OfferedInterfaces(offered = {OfferableBrokerService.class})
@RequiredInterfaces(required = {RequirableMessagePublisher.class})

public class MasterPubSub extends AbstractComponent implements BrokerService {

    protected Broker broker = new Broker();
    private PubSubInBoundPort inBoundPort;

    public MasterPubSub(String inBoundPortUri) throws Exception {
        super(1, 0);
        inBoundPort = new PubSubInBoundPort(inBoundPortUri,this);
        // add the port to the set of ports of the component
        this.addPort(inBoundPort);
        // publish the port
        inBoundPort.publishPort();

        this.tracer.setTitle("pubsub");
        this.tracer.setRelativePosition(1, 0);

    }

    public MasterPubSub() throws Exception {
        super(1, 0);


        inBoundPort = new PubSubInBoundPort(this);
        // add the port to the set of ports of the component
        this.addPort(inBoundPort);
        // publish the port
        inBoundPort.publishPort();

        this.tracer.setTitle("pubsub");
        this.tracer.setRelativePosition(1, 0);

    }

    @Override
    public void start() throws ComponentStartException {
        super.start();
        this.logMessage("starting pubsub component.");
    }


    @Override
    public void finalise() throws Exception {
        super.finalise();
        this.logMessage("stopping pubsub component.");
        broker.shutdown();

    }


    @Override
    public void shutdown() throws ComponentShutdownException {
        unpublishPorts();
        super.shutdown();
    }

    @Override
    public void shutdownNow() throws ComponentShutdownException {
        unpublishPorts();
        super.shutdownNow();
    }

    private void unpublishPorts() throws ComponentShutdownException {
        try {
            PortI[] p = this.findPortsFromInterface(OfferableBrokerService.class);
            p[0].unpublishPort();
            p = this.findPortsFromInterface(OfferableMessageReceiver.class);
            p[0].unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
    }


    @Override
    public void publish(Message message) throws Exception {
        this.logMessage("pubsub publishing: " + message.getContent() + " -> " + message.getTopic());
//        broker.publishRoot(message);
        broker.publish(message);
    }

    @Override
    public void subscribe(Topic topic, String subscriberPort) throws Exception {
        this.logMessage("pubsub subscribing: " + subscriberPort + " -> " + topic);


        this.broker.subscribe(topic, MessagePublisherHandler.newBuilder(this)
                .setSubInBoundPortUri(subscriberPort)
                .build()
        );

    }

    @Override
    public void subscribe(Topic topic, String subscriberPort, Predicate<Message> filter) throws Exception {
        this.logMessage("pubsub subscribing: " + subscriberPort + " -> " + topic);

        this.broker.subscribe(topic, MessagePublisherHandler.newBuilder(this)
                .setSubInBoundPortUri(subscriberPort)
                .setFilter(topic, filter)
                .build()
        );

    }

    @Override
    public void updateFilter(Topic topic, String subscriberPort, Predicate<Message> filter) throws Exception {
        this.broker.updateFilter(topic, subscriberPort, filter);
    }

    @Override
    public void unsubscribe(Topic topic, String subscriberPort) throws Exception {
        this.logMessage("pubsub removing " + subscriberPort + " subscription from :" + topic);
        broker.unsubscribe(topic, subscriberPort);
    }

    @Override
    public void unsubscribe(String subscriberPort) throws Exception {
        this.logMessage("pubsub removing " + subscriberPort + " subscription from all topics");
        broker.unsubscribe(subscriberPort);
    }


    public String getInBoundPortURI() throws Exception {
        return inBoundPort.getPortURI();
    }


    public static class MessagePublisherHandler implements MessagePublisher {

        private String subInBoundPortUri;
        private MessagePublisherOutBoundPort msgPubOutBoundPort;
        private ConcurrentHashMap<Topic, Predicate<Message>> filters = new ConcurrentHashMap<>();
        private MasterPubSub owner;

        private MessagePublisherHandler(MasterPubSub owner, String subInBoundPortUri) throws Exception {
            this.owner = owner;
            this.msgPubOutBoundPort = new MessagePublisherOutBoundPort(owner);
            owner.addPort(msgPubOutBoundPort);
            this.msgPubOutBoundPort.localPublishPort();
            this.subInBoundPortUri = subInBoundPortUri;


            owner.doPortConnection(
                    msgPubOutBoundPort.getPortURI(),
                    subInBoundPortUri,
                    MessagePublisherServiceConnector.class.getCanonicalName());

        }

        private static MessagePublisherComponentBuilder newBuilder(MasterPubSub masterPubSub) {
            return new MessagePublisherComponentBuilder(masterPubSub);
        }


        public void setFilter(Topic topic, Predicate<Message> filter) {
            if (topic != null)
                this.filters.put(topic, filter);
        }

        public void shutdown() {
            try {

                this.msgPubOutBoundPort.doDisconnection();
                this.msgPubOutBoundPort.unpublishPort();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void sendMessage(Message message) {
            this.msgPubOutBoundPort.sendMessage(message);
        }

        @Override
        public boolean accept(Message message) {
            Predicate<Message> filter = filters.get(message.getTopic());
            if (filter == null)
                return true;

            return filter.test(message);
        }

        @Override
        public String getKey() {
            return subInBoundPortUri;
        }

        protected static class MessagePublisherComponentBuilder {
            private String subInBoundPortUri;
            private Topic topic;
            private Predicate<Message> filter;
            private MasterPubSub owner;

            public MessagePublisherComponentBuilder(MasterPubSub owner) {
                this.owner = owner;
            }

            protected MessagePublisherComponentBuilder setSubInBoundPortUri(String subInBoundPortUri) {
                Objects.requireNonNull(subInBoundPortUri);
                this.subInBoundPortUri = subInBoundPortUri;
                return this;
            }

            protected MessagePublisherComponentBuilder setFilter(Topic topic, Predicate<Message> filter) {
                this.topic = Objects.requireNonNull(topic);
                this.filter = Objects.requireNonNull(filter);
                return this;

            }

            protected MessagePublisher build() throws Exception {
                MessagePublisherHandler msgPub = new MessagePublisherHandler(owner, subInBoundPortUri);
                msgPub.setFilter(topic, filter);
                return msgPub;
            }
        }

    }

}

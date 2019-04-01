package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.connectors.MessagePublisherServiceConnector;
import fr.sorbonne_u.pubsub.interfaces.MessagePublisher;
import fr.sorbonne_u.pubsub.interfaces.RequirableMessagePublisher;
import fr.sorbonne_u.pubsub.port.MessagePublisherOutBoundPort;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@RequiredInterfaces(required = RequirableMessagePublisher.class)
public class MessagePublisherComponent extends AbstractComponent implements MessagePublisher {

    private String subInBoundPortUri;
    private MessagePublisherOutBoundPort msgPubOutBoundPort;
    private ConcurrentHashMap<Topic, Predicate<Message>> filters = new ConcurrentHashMap<>();

    private MessagePublisherComponent(String subInBoundPortUri) throws Exception {
        super(1, 0);


        this.msgPubOutBoundPort = new MessagePublisherOutBoundPort(this);
        this.addPort(msgPubOutBoundPort);
        this.msgPubOutBoundPort.localPublishPort();
        this.subInBoundPortUri = subInBoundPortUri;

    }

    public static MessagePublisherComponentBuilder newBuilder() {
        return new MessagePublisherComponentBuilder();
    }


    public void setFilter(Topic topic, Predicate<Message> filter) {
        if (topic != null)
            this.filters.put(topic, filter);
    }

    @Override
    public void finalise() throws Exception {
        this.logMessage("stopping MessagePublisher component.");
        // This is the place where to clean up resources, such as
        // disconnecting and unpublishing ports that will be destroyed
        // when shutting down.
        this.msgPubOutBoundPort.doDisconnection();
        this.msgPubOutBoundPort.unpublishPort();

        // This called at the end to make the component internal
        // state move to the finalised state.
        super.finalise();
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
            MessagePublisherComponent msgPub = new MessagePublisherComponent(subInBoundPortUri);
            msgPub.setFilter(topic, filter);
            msgPub.doPortConnection(
                    msgPub.msgPubOutBoundPort.getPortURI(),
                    subInBoundPortUri,
                    MessagePublisherServiceConnector.class.getCanonicalName());

            return msgPub;
        }
    }

}

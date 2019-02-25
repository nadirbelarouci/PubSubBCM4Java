package fr.sorbonne_u.pubsub.components;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.*;
import fr.sorbonne_u.pubsub.port.MessagePublisherOutBoundPort;
import fr.sorbonne_u.pubsub.port.PubSubComponentInBoundPort;

import java.util.Objects;

@OfferedInterfaces(offered = OfferableBrokerService.class)
@RequiredInterfaces(required = RequirableMessagePublisher.class)
public class PubSub extends AbstractComponent implements BrokerService, MessagePublisher {

    private Broker broker = Broker.getInstance();
    private MessagePublisherOutBoundPort msgPubOutBoundPort;

    public PubSub(String uri, String pubSubInBoundPortUri, String msgPubOutBoundPortUri) throws Exception {
        super(uri, 1, 0);

        Objects.requireNonNull(uri);
        Objects.requireNonNull(pubSubInBoundPortUri);
        Objects.requireNonNull(msgPubOutBoundPortUri);

        PortI p = new PubSubComponentInBoundPort(pubSubInBoundPortUri, this);
        // add the port to the set of ports of the component
        this.addPort(p);
        // publish the port
        p.publishPort();


        this.msgPubOutBoundPort = new MessagePublisherOutBoundPort(msgPubOutBoundPortUri, this);
        this.addPort(msgPubOutBoundPort);
        this.msgPubOutBoundPort.localPublishPort();

        this.tracer.setTitle("pubsub");
        this.tracer.setRelativePosition(1, 0);

    }

    @Override
    public void start() throws ComponentStartException {
        this.logMessage("starting provider component.");
        super.start();
    }


    @Override
    public void finalise() throws Exception {
        this.logMessage("stopping provider component.");
        super.finalise();
    }


    @Override
    public void shutdown() throws ComponentShutdownException {
        try {
            PortI[] p = this.findPortsFromInterface(BrokerService.class);
            p[0].unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

    @Override
    public void shutdownNow() throws ComponentShutdownException {
        try {
            PortI[] p = this.findPortsFromInterface(BrokerService.class);
            p[0].unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdownNow();
    }


    @Override
    public void publish(Message message) throws Exception {
        this.logMessage("pubsub publishing.");
        broker.publish(message);

    }

    @Override
    public void subscribe(Topic topic, MessageReceiver messageReceiver) throws Exception {
        this.logMessage("pubsub subscribe.");
//        this.broker.subscribe(topic, messageReceiver);

    }

    @Override
    public void unsubscribe(Topic topic, MessageReceiver messageReceiver) throws Exception {
        this.logMessage("pubsub subscribe.");
//        broker.unsubscribe(topic, messageReceiver);
    }

    @Override
    public void unsubscribe(MessageReceiver messageReceiver) throws Exception {
        this.logMessage("pubsub subscribe.");
//        broker.unsubscribe(messageReceiver);
    }

    @Override
    public void update(Message message) throws Exception {
        this.msgPubOutBoundPort.update(message);
        this.logMessage("pubsub notifying a subscriber: " + message.getContent());
    }
}

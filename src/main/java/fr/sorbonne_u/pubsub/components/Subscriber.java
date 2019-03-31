package fr.sorbonne_u.pubsub.components;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.MessageReceiver;
import fr.sorbonne_u.pubsub.interfaces.OfferableMessageReceiver;
import fr.sorbonne_u.pubsub.interfaces.RequirableSubscriberService;
import fr.sorbonne_u.pubsub.interfaces.SubscriberService;
import fr.sorbonne_u.pubsub.port.MessageReceiverInBoundPort;
import fr.sorbonne_u.pubsub.port.SubscriberInBoundPort;
import fr.sorbonne_u.pubsub.port.SubscriberOutBoundPort;

import java.util.Objects;

@RequiredInterfaces(required = RequirableSubscriberService.class)
@OfferedInterfaces(offered = OfferableMessageReceiver.class)
public class Subscriber extends AbstractComponent implements SubscriberService, MessageReceiver {

    private SubscriberOutBoundPort subscriberOutBoundPort;
    private SubscriberInBoundPort subscriberInBoundPort;
    // add field MessageReceiverInBounPort
    private MessageReceiverInBoundPort messageReceiverInBoundPort;


    public Subscriber(String uri, String subOutBoundPortUri, String msgInBoundPortUri) throws Exception {
        super(uri, 1, 1);
        Objects.requireNonNull(uri);
        Objects.requireNonNull(subOutBoundPortUri);
        Objects.requireNonNull(msgInBoundPortUri);



        PortI p = new MessageReceiverInBoundPort(msgInBoundPortUri, this);
        // add the port to the set of ports of the component
        this.addPort(p);
        // publish the port
        p.publishPort();

        // add inBoundPort URI in SubscriberOutBoundPort constructor
        this.subscriberOutBoundPort = new SubscriberOutBoundPort(subOutBoundPortUri, this);
        this.subscriberInBoundPort = new SubscriberInBoundPort(subOutBoundPortUri, this, this.subscriberOutBoundPort );
        this.addPort(subscriberOutBoundPort);
        this.subscriberOutBoundPort.localPublishPort();


        this.tracer.setTitle("Subscriber");
        this.tracer.setRelativePosition(1, 1);

    }

    @Override
    public void start() throws ComponentStartException {
        super.start();
        this.logMessage("starting publisher component.");

    }

    /**
     * @see fr.sorbonne_u.components.AbstractComponent#finalise()
     */
    @Override
    public void finalise() throws Exception {
        this.logMessage("stopping publisher component.");
        // This is the place where to clean up resources, such as
        // disconnecting and unpublishing ports that will be destroyed
        // when shutting down.
        this.subscriberOutBoundPort.doDisconnection();
        this.subscriberOutBoundPort.unpublishPort();

        // This called at the end to make the component internal
        // state move to the finalised state.
        super.finalise();
    }

    @Override
    public void shutdown() throws ComponentShutdownException {
        try {
            PortI[] p = this.findPortsFromInterface(OfferableMessageReceiver.class);
            p[0].unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

    @Override
    public void shutdownNow() throws ComponentShutdownException {
        try {
            PortI[] p = this.findPortsFromInterface(OfferableMessageReceiver.class);
            p[0].unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdownNow();
    }

    @Override
    public void subscribe(Topic topic) throws Exception {
        this.logMessage("subscriber subscribe to a topic: " + topic);
        this.subscriberOutBoundPort.subscribe(topic);
    }

    @Override
    public void subscribe(Topic topic, Filter filter) throws Exception {
        this.logMessage("subscriber subscribe to a topic: " + topic);
        this.subscriberOutBoundPort.subscribe(topic, filter);
    }

    @Override
    public void unsubscribe(Topic topic) throws Exception{
        this.logMessage("subscriber unsubscribe from a topic: " + topic);
        this.subscriberOutBoundPort.unsubscribe(topic);

    }

    @Override
    public void unsubscribe() throws Exception{
        this.logMessage("subscriber unsubscribe from all topics.");
        this.subscriberOutBoundPort.unsubscribe();

    }

    @Override
    public void receiveMessage(Message message) {
        this.logMessage("subscriber received a message: " + message.getContent());
    }
}

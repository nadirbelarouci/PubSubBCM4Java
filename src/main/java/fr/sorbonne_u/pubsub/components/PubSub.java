package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.connectors.PublisherServiceConnector;
import fr.sorbonne_u.pubsub.connectors.SubscriberServiceConnector;
import fr.sorbonne_u.pubsub.interfaces.*;
import fr.sorbonne_u.pubsub.port.PublisherOutBoundPort;
import fr.sorbonne_u.pubsub.port.SubscriberInBoundPort;
import fr.sorbonne_u.pubsub.port.SubscriberOutBoundPort;

@OfferedInterfaces(offered = {OfferableBrokerService.class, OfferableMessageReceiver.class})
@RequiredInterfaces(required = {RequirableMessagePublisher.class, RequirableSubscriberService.class, RequirablePublisherService.class})
public class PubSub extends MasterPubSub implements MessageReceiver {
    private SubscriberOutBoundPort subscriberOutBoundPort;
    private PublisherOutBoundPort publisherOutBoundPort;

    public PubSub(String uri) throws Exception {
        super(uri);
    }

    public PubSub() throws Exception {
        super();

        SubscriberInBoundPort subscriberInBoundPort = new SubscriberInBoundPort(this);
        // add the port to the set of ports of the component
        this.addPort(subscriberInBoundPort);
        // publish the port
        subscriberInBoundPort.publishPort();

        this.subscriberOutBoundPort = new SubscriberOutBoundPort(this, subscriberInBoundPort.getPortURI());
        this.addPort(subscriberOutBoundPort);
        this.subscriberOutBoundPort.localPublishPort();


        this.publisherOutBoundPort = new PublisherOutBoundPort(this);
        this.addPort(publisherOutBoundPort);
        this.publisherOutBoundPort.localPublishPort();


    }

    @Override
    public void finalise() throws Exception {
        super.finalise();
        this.subscriberOutBoundPort.doDisconnection();
        this.subscriberOutBoundPort.unpublishPort();
        this.publisherOutBoundPort.doDisconnection();
        this.publisherOutBoundPort.unpublishPort();
    }

    @Override
    public void publish(Message message) throws Exception {
        this.publisherOutBoundPort.publish(message);
    }

    @Override
    public void receiveMessage(Message message) throws Exception {
        broker.publish(message);
    }

    public void subscribe() {
        subscriberOutBoundPort.subscribe(Topic.ROOT);
    }

    public void doPortConnection(String inBoundURI) throws Exception {
        this.doPortConnection(
                subscriberOutBoundPort.getPortURI(),
                inBoundURI,
                SubscriberServiceConnector.class.getCanonicalName());

        super.doPortConnection(
                publisherOutBoundPort.getPortURI(),
                inBoundURI,
                PublisherServiceConnector.class.getCanonicalName());
    }

}

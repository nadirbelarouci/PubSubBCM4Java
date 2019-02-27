package fr.sorbonne_u.pubsub.components;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.OfferedInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.pubsub.Filter;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.BrokerService;
import fr.sorbonne_u.pubsub.interfaces.OfferableBrokerService;
import fr.sorbonne_u.pubsub.port.PubSubComponentInBoundPort;

import java.util.Objects;

@OfferedInterfaces(offered = OfferableBrokerService.class)
public class PubSub extends AbstractComponent implements BrokerService {

    private Broker broker = Broker.getInstance();

    public PubSub(String uri, String pubSubInBoundPortUri) throws Exception {
        super(uri, 1, 0);

        Objects.requireNonNull(uri);
        Objects.requireNonNull(pubSubInBoundPortUri);


        PortI p = new PubSubComponentInBoundPort(pubSubInBoundPortUri, this);
        // add the port to the set of ports of the component
        this.addPort(p);
        // publish the port
        p.publishPort();


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
    }


    @Override
    public void shutdown() throws ComponentShutdownException {
        try {
            PortI[] p = this.findPortsFromInterface(OfferableBrokerService.class);
            p[0].unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

    @Override
    public void shutdownNow() throws ComponentShutdownException {
        try {
            PortI[] p = this.findPortsFromInterface(OfferableBrokerService.class);
            p[0].unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdownNow();
    }


    @Override
    public void publish(Message message) throws Exception {
        this.logMessage("pubsub publishing: " + message.getContent() + " -> " + message.getTopic());
//        broker.publish(message);
    }

    @Override
    public void subscribe(Topic topic, String subscriberPort) throws Exception {
        this.logMessage("pubsub subscribing: " + subscriberPort + " -> " + topic);
//        this.broker.subscribe(topic, messageReceiver);

    }

    @Override
    public void subscribe(Topic topic, String subscriberPort, Filter filter) throws Exception {
        this.logMessage("pubsub subscribing: " + subscriberPort + " -> " + topic);

    }

    @Override
    public void updateFilter(String subscriberPort, Filter filter) throws Exception {

    }

    @Override
    public void unsubscribe(Topic topic, String subscriberPort) throws Exception {
        this.logMessage("pubsub removing " + subscriberPort + " subscription from :" + topic);
//        broker.unsubscribe(topic, messageReceiver);
    }

    @Override
    public void unsubscribe(String subscriberPort) throws Exception {
        this.logMessage("pubsub removing " + subscriberPort + " subscription from all topics");

//        broker.unsubscribe(messageReceiver);
    }


}

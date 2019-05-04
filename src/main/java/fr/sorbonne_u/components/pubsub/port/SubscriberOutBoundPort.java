package fr.sorbonne_u.components.pubsub.port;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;
import fr.sorbonne_u.components.pubsub.interfaces.SubscriberService;

import java.util.function.Predicate;

/**
 * The {@code SubscriberOutBoundPort} is an implementation of the Subscriber out-bound port.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.components.Subscriber
 */
public class SubscriberOutBoundPort extends AbstractOutboundPort implements SubscriberService.Required {
    private String subId;

    /**
     * Creates a new {@code SubscriberOutBoundPort} instance with a specific URI.
     *
     * @param uri   The port URI
     * @param owner The component owner
     * @param subId The subscriber ID
     * @throws Exception
     */
    public SubscriberOutBoundPort(String uri, ComponentI owner, String subId) throws Exception {
        super(uri, SubscriberService.Required.class, owner);
        this.subId = subId;
    }

    /**
     * Creates a new {@code SubscriberOutBoundPort} instance with a random unique URI.
     *
     * @param owner The component owner
     * @param subId The subscriber ID
     * @throws Exception
     */
    public SubscriberOutBoundPort(ComponentI owner, String subId) throws Exception {
        super(SubscriberService.Required.class, owner);
        this.subId = subId;
    }

    @Override
    public void subscribe(Topic topic) {
        ((SubscriberService.Required) this.connector).subscribe(topic);
    }

    @Override
    public void subscribe(Topic topic, Predicate<Message> filter) {
        ((SubscriberService.Required) this.connector).subscribe(topic, filter);
    }

    @Override
    public void filter(Topic topic, Predicate<Message> filter) {
        ((SubscriberService.Required) this.connector).filter(topic, filter);

    }

    @Override
    public void unsubscribe(Topic topic) {
        ((SubscriberService.Required) this.connector).unsubscribe(topic);
    }

    @Override
    public void unsubscribe() {
        ((SubscriberService.Required) this.connector).unsubscribe();
    }

    @Override
    public String getSubId() {
        return subId;
    }

}

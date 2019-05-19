package fr.sorbonne_u.components.pubsub.port;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;
import fr.sorbonne_u.components.pubsub.interfaces.PubSubService;
import fr.sorbonne_u.components.pubsub.interfaces.Subscription;

import java.util.function.Predicate;

/**
 * The {@code PublisherOutBoundPort} is an implementation of the PubSub in-bound port,
 * note that a PubSub can only have one in-bound port.
 * This in-bound port will be used by the required interfaces of the {@code Publisher}
 * and the {@code Subscriber} components.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.components.PubSub
 * @see fr.sorbonne_u.components.pubsub.components.Subscriber
 * @see fr.sorbonne_u.components.pubsub.components.Publisher
 * @see Subscription
 */
public class PubSubInBoundPort extends AbstractInboundPort implements PubSubService.Offered {
    /**
     * Creates a new {@code PubSubInBoundPort} instance with a specific URI.
     *
     * @param uri   The port URI
     * @param owner The component owner
     * @throws Exception
     */
    public PubSubInBoundPort(String uri, ComponentI owner) throws Exception {
        super(uri, PubSubService.Offered.class, owner);
    }

    /**
     * Creates a new {@code PubSubInBoundPort} instance with a random unique URI.
     *
     * @param owner The component owner
     * @throws Exception
     */
    public PubSubInBoundPort(ComponentI owner) throws Exception {
        super(PubSubService.Offered.class, owner);
    }

    @Override
    public void publish(Message message) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((PubSubService) this.getOwner()).publish(message);
                        return null;
                    }
                });

    }

    @Override
    public void subscribe(String subId, Topic topic) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((PubSubService) this.getOwner()).subscribe(subId, topic);
                        return null;
                    }
                });
    }

    @Override
    public void subscribe(String subId, Topic topic, Predicate<Message> filter) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((PubSubService) this.getOwner()).subscribe(subId, topic, filter);
                        return null;
                    }
                });
    }

    @Override
    public void unsubscribe(String subId, Topic topic) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((PubSubService) this.getOwner()).unsubscribe(subId, topic);
                        return null;
                    }
                });
    }

    @Override
    public void filter(String subId, Topic topic, Predicate<Message> filter) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((PubSubService) this.getOwner()).filter(subId, topic, filter);
                        return null;
                    }
                });
    }


    @Override
    public void unsubscribe(String subId) throws Exception {
        this.getOwner().handleRequestAsync(
                new AbstractComponent.AbstractService<Void>() {
                    @Override
                    public Void call() throws Exception {
                        ((PubSubService) this.getOwner()).unsubscribe(subId);
                        return null;
                    }
                });
    }
}

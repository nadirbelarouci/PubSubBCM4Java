package fr.sorbonne_u.components.pubsub.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;
import fr.sorbonne_u.components.pubsub.exceptions.CallOfferedMethodException;
import fr.sorbonne_u.components.pubsub.interfaces.PubSubService;
import fr.sorbonne_u.components.pubsub.interfaces.SubscriberService;
import fr.sorbonne_u.components.pubsub.port.SubscriberOutBoundPort;

import java.util.function.Predicate;

/**
 * The {@code SubscriberServiceConnector} class connects between the PubSub in-bound port and
 * the Subscriber out-bound port,  the connection between the two ports
 * * get established just after the Subscriber instantiation.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.port.PubSubOutBoundPort
 * @see fr.sorbonne_u.components.pubsub.port.SubscriberInBoundPort
 */
public class SubscriberServiceConnector extends AbstractConnector implements SubscriberService.Required {


    @Override
    public String getSubId() {

        if (!(this.requiring instanceof SubscriberOutBoundPort))
            throw new ClassCastException();

        return ((SubscriberService.Required) this.requiring).getSubId();
    }

    @Override
    public void subscribe(Topic topic) {

        try {
            ((PubSubService.Offered) this.offering).subscribe(getSubId(), topic);
        } catch (Exception e) {
            throw new CallOfferedMethodException(e);
        }

    }

    @Override
    public void subscribe(Topic topic, Predicate<Message> filter) {
        try {
            ((PubSubService.Offered) this.offering).subscribe(getSubId(), topic, filter);
        } catch (Exception e) {
            throw new CallOfferedMethodException(e);

        }
    }

    @Override
    public void filter(Topic topic, Predicate<Message> filter) {
        try {
            ((PubSubService.Offered) this.offering).filter(getSubId(), topic, filter);
        } catch (Exception e) {
            throw new CallOfferedMethodException(e);

        }
    }

    @Override
    public void unsubscribe(Topic topic) {
        try {
            ((PubSubService.Offered) this.offering).unsubscribe(getSubId(), topic);
        } catch (Exception e) {
            throw new CallOfferedMethodException(e);

        }

    }

    @Override
    public void unsubscribe() {
        try {
            ((PubSubService.Offered) this.offering).unsubscribe(this.requiringPortURI);

        } catch (Exception e) {
            throw new CallOfferedMethodException(e);

        }

    }

}

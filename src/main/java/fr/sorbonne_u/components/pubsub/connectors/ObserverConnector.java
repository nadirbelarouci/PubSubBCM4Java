package fr.sorbonne_u.components.pubsub.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.interfaces.Observer;

/**
 * The {@code ObserverConnector} class connects between the PubSub out-bound port and
 * the Subscriber in-bound port, at each subscription the PubSub establish a  connection between the two ports.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.port.PubSubOutBoundPort
 * @see fr.sorbonne_u.components.pubsub.port.SubscriberInBoundPort
 */
public class ObserverConnector extends AbstractConnector implements Observer.Required {
    @Override
    public void notify(Message message) throws Exception {
        message.setTimestamp(System.currentTimeMillis());
        ((Observer.Offered) this.offering).notify(message);
    }
}

package fr.sorbonne_u.components.pubsub.connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.exceptions.CallOfferedMethodException;
import fr.sorbonne_u.components.pubsub.interfaces.PubSubService;
import fr.sorbonne_u.components.pubsub.interfaces.PublisherService;

/**
 * The {@code PublisherServiceConnector} class connects between the PubSub in-bound port and
 * the Publisher out-bound port, the connection between the two ports
 * get established just after the Publisher instantiation.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see fr.sorbonne_u.components.pubsub.port.PubSubInBoundPort
 * @see fr.sorbonne_u.components.pubsub.port.PublisherOutBoundPort
 */
public class PublisherServiceConnector extends AbstractConnector implements PublisherService.Required {

    @Override
    public void publish(Message message) {

        try {
            ((PubSubService.Offered) this.offering).publish(message);
        } catch (Exception e) {
            throw new CallOfferedMethodException(e);
        }
    }
}

package fr.sorbonne_u.pubsub.components;


import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.connectors.PublisherServiceConnector;
import fr.sorbonne_u.pubsub.interfaces.PublisherService;
import fr.sorbonne_u.pubsub.interfaces.RequirablePublisherService;
import fr.sorbonne_u.pubsub.port.PublisherOutBoundPort;

import java.util.Objects;

@RequiredInterfaces(required = RequirablePublisherService.class)
public class Publisher extends AbstractComponent implements PublisherService {

    private PublisherOutBoundPort publisherOutBoundPort;

    public Publisher(String uri, String outBoundPortUri) throws Exception {
        super(uri, 1, 1);
        Objects.requireNonNull(uri);
        Objects.requireNonNull(outBoundPortUri);

        this.publisherOutBoundPort = new PublisherOutBoundPort(outBoundPortUri, this);
        this.addPort(publisherOutBoundPort);
        this.publisherOutBoundPort.localPublishPort();

        this.tracer.setTitle("Publisher");
        this.tracer.setRelativePosition(1, 2);

    }

    public Publisher() throws Exception {
        super(1, 1);


        this.publisherOutBoundPort = new PublisherOutBoundPort(this);
        this.addPort(publisherOutBoundPort);
        this.publisherOutBoundPort.localPublishPort();

        this.tracer.setTitle("Publisher");
        this.tracer.setRelativePosition(1, 2);

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
        this.publisherOutBoundPort.doDisconnection();
        this.publisherOutBoundPort.unpublishPort();

        // This called at the end to make the component internal
        // state move to the finalised state.
        super.finalise();
    }

    @Override
    public void publish(Message message) throws Exception {
        this.logMessage("publisher publishing: " + message.getContent() + " -> " + message.getTopic() + ".");
        this.publisherOutBoundPort.publish(message);
    }

    public void doPortConnection(String inBoundURI) throws Exception {
        super.doPortConnection(
                publisherOutBoundPort.getPortURI(),
                inBoundURI,
                PublisherServiceConnector.class.getCanonicalName());
    }
}

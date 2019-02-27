package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.RequiredInterfaces;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.MessagePublisher;
import fr.sorbonne_u.pubsub.interfaces.RequirableMessagePublisher;
import fr.sorbonne_u.pubsub.interfaces.RequirablePublisherService;
import fr.sorbonne_u.pubsub.port.MessagePublisherOutBoundPort;

import java.util.Objects;

@RequiredInterfaces(required = RequirableMessagePublisher.class)
public class MessageHandlerComponent extends AbstractComponent implements MessagePublisher {

    private MessagePublisherOutBoundPort msgPubOutBoundPort;

    public MessageHandlerComponent(String uri, String msgPubOutBoundPortUri) throws Exception {
        super(uri, 1, 0);
        Objects.requireNonNull(uri);
        Objects.requireNonNull(msgPubOutBoundPortUri);


        this.msgPubOutBoundPort = new MessagePublisherOutBoundPort(msgPubOutBoundPortUri, this);
        this.addPort(msgPubOutBoundPort);
        this.msgPubOutBoundPort.localPublishPort();
    }


    @Override
    public void shutdown() throws ComponentShutdownException {
        try {
            PortI[] p = this.findPortsFromInterface(RequirablePublisherService.class);
            p[0].unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdown();
    }

    @Override
    public void shutdownNow() throws ComponentShutdownException {
        try {
            PortI[] p = this.findPortsFromInterface(RequirablePublisherService.class);
            p[0].unpublishPort();
        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.shutdownNow();
    }

    @Override
    public void sendMessage(Message message) {

    }

    @Override
    public boolean accept() {
        return false;
    }
}

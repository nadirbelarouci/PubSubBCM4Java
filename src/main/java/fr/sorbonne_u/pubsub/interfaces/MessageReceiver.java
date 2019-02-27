package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Message;

public interface MessageReceiver {
    void receiveMessage(Message message) throws Exception;
}
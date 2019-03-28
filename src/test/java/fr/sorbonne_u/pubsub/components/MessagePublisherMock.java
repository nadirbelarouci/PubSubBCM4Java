package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.MessagePublisher;

import java.util.Objects;

public class MessagePublisherMock implements MessagePublisher {
    private Message message = null;

    private String name;

    public MessagePublisherMock(String name) {
        this.name = name;
    }


    @Override
    public void sendMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {

        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessagePublisherMock)) return false;
        MessagePublisherMock that = (MessagePublisherMock) o;
        return name.equals(that.name);
    }

    @Override
    public String toString() {
        return "MessagePublisherMock{" +
                ", name='" + name + '\'' +
                '}' + " - " + hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

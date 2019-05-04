package fr.sorbonne_u.components.pubsub.components;

import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;
import fr.sorbonne_u.components.pubsub.interfaces.Subscribable;

import java.util.Objects;
import java.util.function.Predicate;

public class SubscribableMock implements Subscribable {
    private Message message = null;

    private String name;

    public SubscribableMock(String name) {
        this.name = name;
    }


    @Override
    public void notify(Message message) {
        this.message = message;
    }

    public Message getMessage() {

        return message;
    }

    @Override
    public void filter(Topic topic, Predicate<Message> filter) {
        // TODO add filter
    }

    @Override
    public String getSubId() {
        return name;
    }

    @Override
    public void shutdown() {
        // TODO add shutdown
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscribableMock)) return false;
        SubscribableMock that = (SubscribableMock) o;
        return name.equals(that.name);
    }

    @Override
    public String toString() {
        return "SubscribableMock{" +
                ", name='" + name + '\'' +
                '}' + " - " + hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

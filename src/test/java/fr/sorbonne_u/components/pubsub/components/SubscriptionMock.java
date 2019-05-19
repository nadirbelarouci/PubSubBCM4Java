package fr.sorbonne_u.components.pubsub.components;

import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.interfaces.Subscription;

import java.util.Objects;
import java.util.function.Predicate;

public class SubscriptionMock implements Subscription {
    private Message message = null;

    private String name;

    public SubscriptionMock(String name) {
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
    public void filter(Predicate<Message> filter) {
        // TODO add filter
    }

    @Override
    public String getSubId() {
        return name;
    }

    @Override
    public void end() {
        // TODO add end
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubscriptionMock)) return false;
        SubscriptionMock that = (SubscriptionMock) o;
        return name.equals(that.name);
    }

    @Override
    public String toString() {
        return "SubscriptionMock{" +
                ", name='" + name + '\'' +
                '}' + " - " + hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

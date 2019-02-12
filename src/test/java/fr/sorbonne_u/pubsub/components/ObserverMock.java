package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;

import java.util.Objects;

public class ObserverMock implements Observer {
    private Message message = null;

    private String name;

    public ObserverMock(String name) {
        this.name = name;
    }

    @Override
    public void update(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ObserverMock)) return false;
        ObserverMock that = (ObserverMock) o;
        return Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), name);
    }
}
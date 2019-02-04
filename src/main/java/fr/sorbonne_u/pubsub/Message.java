package fr.sorbonne_u.pubsub;

import java.util.Objects;

public class Message {
    private String id;
    private long timestamp;
    private String owner;
    private Object content;
    private BasicProperties basicProperties;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public BasicProperties getBasicProperties() {
        return basicProperties;
    }

    public void setBasicProperties(BasicProperties basicProperties) {
        this.basicProperties = basicProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return getTimestamp() == message.getTimestamp() &&
                Objects.equals(getId(), message.getId()) &&
                Objects.equals(getOwner(), message.getOwner()) &&
                Objects.equals(getContent(), message.getContent()) &&
                Objects.equals(getBasicProperties(), message.getBasicProperties());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimestamp(), getOwner(), getContent(), getBasicProperties());
    }
}

package fr.sorbonne_u.pubsub;

import java.util.Objects;

public class Message {
    private final String id;
    private final long timestamp;
    private final String owner;
    private final Object content;
    private final BasicProperties basicProperties;

    public Message(String id, long timestamp, String owner, Object content, BasicProperties basicProperties) {
        this.id = id;
        this.timestamp = timestamp;
        this.owner = owner;
        this.content = content;
        this.basicProperties = basicProperties;
    }

    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }

    public String getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getOwner() {
        return owner;
    }

    public Object getContent() {
        return content;
    }

    public BasicProperties getBasicProperties() {
        return basicProperties;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", owner='" + owner + '\'' +
                ", content=" + content +
                ", basicProperties=" + basicProperties +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return getTimestamp() == message.getTimestamp() &&
                Objects.equals(getId(), message.getId()) &&
                Objects.equals(getOwner(), message.getOwner()) &&
                getContent().equals(message.getContent()) &&
                Objects.equals(getBasicProperties(), message.getBasicProperties());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimestamp(), getOwner(), getContent(), getBasicProperties());
    }

    public static class MessageBuilder {
        private String id;
        private long timestamp = -1;
        private String owner;
        private Object content;
        private BasicProperties basicProperties;

        private MessageBuilder() {

        }

        public MessageBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public MessageBuilder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public MessageBuilder setOwner(String owner) {
            this.owner = owner;
            return this;
        }

        public MessageBuilder setContent(Object content) {
            this.content = content;
            return this;
        }

        public MessageBuilder setBasicProperties(BasicProperties basicProperties) {
            this.basicProperties = basicProperties;
            return this;
        }

        public Message build() {
            return new Message(id, timestamp == -1 ? System.currentTimeMillis() : timestamp,
                    owner, content, basicProperties);
        }
    }
}

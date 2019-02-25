package fr.sorbonne_u.pubsub;

import java.util.Objects;

public class Message {
    private final String id;
    private final long timestamp;
    private final String owner;
    private final Object content;
    private final BasicProperties basicProperties;
    private final Topic topic;

    private Message(String id, long timestamp, String owner, Object content, Topic topic, BasicProperties basicProperties) {
        this.id = id;
        this.timestamp = timestamp;
        this.owner = owner;
        this.content = content;
        this.basicProperties = basicProperties;
        this.topic = topic;
    }

    public static MessageBuilder newBuilder(Topic topic) {
        return new MessageBuilder(topic);
    }

    public static MessageBuilder newBuilder(String topic) {
        return new MessageBuilder(topic);
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


    public Topic getTopic() {
        return topic;
    }


    public static class MessageBuilder {
        private String id;
        private long timestamp = -1;
        private String owner;
        private Object content;
        private Topic topic;
        private BasicProperties basicProperties = new BasicProperties();

        private MessageBuilder(Topic topic) {
            setTopic(topic);
        }

        private MessageBuilder(String topic) {
            setTopic(Topic.newBuilder(topic).build());
        }

        public MessageBuilder setTopic(Topic topic) {
            this.topic = Objects.requireNonNull(topic, "Topic cannot be null");
            return this;
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

        public MessageBuilder addProperty(String key, String value) {
            basicProperties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, byte value) {
            basicProperties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, short value) {
            basicProperties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, char value) {
            basicProperties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, int value) {
            basicProperties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, long value) {
            basicProperties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, float value) {
            basicProperties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, double value) {
            basicProperties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, boolean value) {
            basicProperties.put(key, value);
            return this;
        }


        public Message build() {
            return new Message(id, timestamp == -1 ? System.currentTimeMillis() : timestamp,
                    owner, content, topic, basicProperties);
        }
    }
}

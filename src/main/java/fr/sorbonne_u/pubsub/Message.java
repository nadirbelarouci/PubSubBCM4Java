package fr.sorbonne_u.pubsub;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class Message {
    private final String id;
    private final String owner;
    private final Serializable content;
    private final Topic topic;
    private Map<String, Object> properties;
    private long timestamp;


    private Message(String id, long timestamp, String owner, Serializable content, Topic topic, Map<String, Object> properties) {
        this.id = id;
        this.timestamp = timestamp;
        this.owner = owner;
        this.content = content;
        this.properties = properties;
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

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getOwner() {
        return owner;
    }

    public Object getContent() {
        return content;
    }

    public Topic getTopic() {
        return topic;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", owner='" + owner + '\'' +
                ", content=" + content +
                ", properties=" + properties +
                '}';
    }


    protected boolean filter(String key, Predicate<Object> predicate) {
        return predicate.test(properties.get(key));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return timestamp == message.timestamp &&
                Objects.equals(id, message.id) &&
                Objects.equals(owner, message.owner) &&
                Objects.equals(content, message.content) &&
                Objects.equals(properties, message.properties) &&
                Objects.equals(topic, message.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, content, properties, topic, timestamp);
    }

    public static class MessageBuilder {
        private String id;
        private long timestamp = -1;
        private String owner;
        private Serializable content;
        private Topic topic;
        private Map<String, Object> properties = new ConcurrentHashMap<>();


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

        public MessageBuilder setContent(Serializable content) {
            this.content = content;
            return this;
        }

        public MessageBuilder addProperty(String key, String value) {
            properties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, byte value) {
            properties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, short value) {
            properties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, char value) {
            properties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, int value) {
            properties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, long value) {
            properties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, float value) {
            properties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, double value) {
            properties.put(key, value);
            return this;
        }

        public MessageBuilder addProperty(String key, boolean value) {
            properties.put(key, value);
            return this;
        }


        public Message build() {
            return new Message(id, timestamp == -1 ? System.currentTimeMillis() : timestamp,
                    owner, content, topic, properties);
        }
    }

}

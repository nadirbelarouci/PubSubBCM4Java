package fr.sorbonne_u.components.pubsub;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code Message} class wraps the data that we need to publish from the publishers to the subscribers.
 *
 * <p>
 * Each {@code Message} instance has:
 * <ul>
 * <li>
 * A unique ID generated by  {@link UUID#randomUUID()}.
 * </li>
 * <li>
 * A timestamp which is set at sending time.
 * </li>
 * <li>
 * An owner which represents the host ip address.
 * </li>
 * <li>
 * The data that is needed to be published.
 * </li>
 * <li>
 * A set of properties i.e key-value pairs.
 * </li>
 * </ul>
 * </p>
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see Message.Builder
 * @see Topic
 * @see fr.sorbonne_u.components.pubsub.interfaces.PublisherService
 * @see fr.sorbonne_u.components.pubsub.interfaces.SubscriberService
 */
public class Message implements Serializable {
    /**
     * Message unique ID.
     */
    private final String id;
    /**
     * Host Ip address.
     */
    private final String owner;
    /**
     * The data to publish.
     */
    private final Serializable content;
    /**
     * The topic that the message will be published on.
     */
    private final Topic topic;

    /**
     * The message properties.
     */
    private final Map<String, Object> properties;

    /**
     * Message timestamp.
     */
    private long timestamp;

    /**
     * Creates a new message using a {@link Builder} instance.
     * <p>
     * Note that the class offers factory methods {@link #newBuilder}
     * to create a new {@code Message}.
     * </p>
     *
     * @param builder The message builder
     * @see #newBuilder(Topic)
     * @see #newBuilder(String)
     */
    private Message(Builder builder) {
        this.id = builder.id;
        this.timestamp = builder.timestamp;
        this.owner = builder.owner;
        this.content = builder.content;
        this.properties = builder.properties;
        this.topic = builder.topic;
    }

    /**
     * Creates a new {@link Message.Builder} from a topic.
     *
     * @param topic A topic
     * @return A {@code Builder}
     */
    public static Builder newBuilder(Topic topic) {
        return new Builder(topic);
    }

    /**
     * Creates a new {@link Message.Builder} from a topic name.
     *
     * @param topic A {@code String}
     * @return A {@code Builder}
     */
    public static Builder newBuilder(String topic) {
        return new Builder(topic);
    }

    /**
     * @return The message ID
     */
    public String getId() {
        return id;
    }

    /**
     * @return The message timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Set timestamp to a new value.
     *
     * @param timestamp a timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return The message owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return The message content.
     */
    public Object getContent() {
        return content;
    }

    /**
     * @return The message topic.
     */
    public Topic getTopic() {
        return topic;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return timestamp == message.timestamp &&
                Objects.equals(id, message.id) &&
                Objects.equals(owner, message.owner) &&
                Objects.equals(content, message.content) &&
                Objects.equals(topic, message.topic);
    }

    /**
     * Returns an {@code int} property value.
     *
     * @param key The property key
     * @return An {@code int} property value or null if key doesn't exist
     * @throws ClassCastException if value is not an instance of {@code Integer}
     */
    public int getInt(String key) {
        return (int) properties.get(key);
    }

    /**
     * Returns a {@code byte} property value.
     *
     * @param key The property key
     * @return A {@code byte}  property value or null if key doesn't exist
     * @throws ClassCastException if value is not an instance of {@code Byte}
     */
    public byte getByte(String key) {
        return (byte) properties.get(key);
    }

    /**
     * Returns a {@code short} property value.
     *
     * @param key The property key
     * @return A {@code short}  property value or null if key doesn't exist
     * @throws ClassCastException if value is not an instance of {@code Short}
     */
    public short getShort(String key) {
        return (short) properties.get(key);
    }

    /**
     * Returns a {@code char} property value.
     *
     * @param key The property key
     * @return A {@code char}  property value or null if key doesn't exist
     * @throws ClassCastException if value is not an instance of {@code Character}
     */
    public char getChar(String key) {
        return (char) properties.get(key);
    }

    /**
     * Returns a {@code long} property value.
     *
     * @param key The property key
     * @return A {@code long}  property value or null if key doesn't exist
     * @throws ClassCastException if value is not an instance of {@code Long}
     */
    public long getLong(String key) {
        return (long) properties.get(key);
    }

    /**
     * Returns a {@code float} property value.
     *
     * @param key The property key
     * @return A {@code float}  property value or null if key doesn't exist
     * @throws ClassCastException if value is not an instance of {@code Float}
     */
    public float getFloat(String key) {
        return (float) properties.get(key);
    }

    /**
     * Returns a {@code double} property value.
     *
     * @param key The property key
     * @return A {@code double}  property value or null if key doesn't exist
     * @throws ClassCastException if value is not an instance of {@code Double}
     */
    public double getDouble(String key) {
        return (double) properties.get(key);
    }

    /**
     * Returns a {@code boolean} property value.
     *
     * @param key The property key
     * @return A {@code boolean}  property value or null if key doesn't exist
     * @throws ClassCastException if value is not an instance of {@code Boolean}
     */
    public boolean getBoolean(String key) {
        return (boolean) properties.get(key);
    }

    /**
     * Returns a {@code String} property value.
     *
     * @param key The property key
     * @return A {@code String}  property value or null if key doesn't exist
     * @throws ClassCastException if value is not an instance of {@code String}
     */
    public String getString(String key) {
        return (String) properties.get(key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, content, topic, timestamp);
    }

    /**
     * The builder class is an implementation of the builder design pattern.
     * <p>
     * The class must control the message properties (i.e accept only String as key, and primitives as values),
     * hence it offers the {@code addProperty} method to do perform this task.
     * the {@code addProperty} method is overloaded to accept all primitive types.
     * </p>
     *
     * @author Nadir Belarouci
     * @author Katia Amichi
     * @see #newBuilder(Topic)
     * @see #newBuilder(String)
     */
    public static class Builder {
        private String id;
        private long timestamp = -1;
        private String owner;
        private Serializable content;
        private Topic topic;
        private final Map<String, Object> properties = new ConcurrentHashMap<>();


        private Builder(Topic topic) {
            setTopic(topic);
            id = Message.class.getSimpleName() + "-" + UUID.randomUUID().toString();
            try {
                owner = InetAddress.getLocalHost().toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private Builder(String topic) {
            this(Topic.of(topic));
        }

        private void setTopic(Topic topic) {
            this.topic = Objects.requireNonNull(topic, "Topic cannot be null");
        }

        /**
         * Sets the message id.
         *
         * @param id The message id
         * @return This builder instance
         */
        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the message timestamp.
         *
         * @param timestamp The message timestamp
         * @return This builder instance
         */
        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * Sets the message owner.
         *
         * @param owner The message owner
         * @return This builder instance
         */
        public Builder setOwner(String owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Sets the message content.
         *
         * @param content The message content
         * @return This builder instance
         */
        public Builder setContent(Serializable content) {
            this.content = content;
            return this;
        }

        /**
         * Add a {@code "String, String"} pair to the message properties.
         *
         * @param key   The property key
         * @param value The property value
         * @return This builder instance
         */
        public Builder addProperty(String key, String value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Add a {@code "String, Byte"} pair to the message properties.
         *
         * @param key   The property key
         * @param value The property value
         * @return This builder instance
         */
        public Builder addProperty(String key, byte value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Add a {@code "String, Short"} pair to the message properties.
         *
         * @param key   The property key
         * @param value The property value
         * @return This builder instance
         */
        public Builder addProperty(String key, short value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Add a {@code "String, Character"} pair to the message properties.
         *
         * @param key   The property key
         * @param value The property value
         * @return This builder instance
         */
        public Builder addProperty(String key, char value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Add a {@code "String, Integer"} pair to the message properties.
         *
         * @param key   The property key
         * @param value The property value
         * @return This builder instance
         */
        public Builder addProperty(String key, int value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Add a {@code "String, Long"} pair to the message properties.
         *
         * @param key   The property key
         * @param value The property value
         * @return This builder instance
         */
        public Builder addProperty(String key, long value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Add a {@code "String, Float"} pair to the message properties.
         *
         * @param key   The property key
         * @param value The property value
         * @return This builder instance
         */
        public Builder addProperty(String key, float value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Add a {@code "String, Double"} pair to the message properties.
         *
         * @param key   The property key
         * @param value The property value
         * @return This builder instance
         */
        public Builder addProperty(String key, double value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Add a {@code "String, Boolean"} pair to the message properties.
         *
         * @param key   The property key
         * @param value The property value
         * @return This builder instance
         */
        public Builder addProperty(String key, boolean value) {
            properties.put(key, value);
            return this;
        }

        /**
         * Build the message.
         *
         * @return A new Message
         */
        public Message build() {
            return new Message(this);
        }

    }

}

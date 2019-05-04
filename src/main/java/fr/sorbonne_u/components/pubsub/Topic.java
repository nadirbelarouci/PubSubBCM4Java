package fr.sorbonne_u.components.pubsub;

import java.io.Serializable;
import java.util.Objects;

/**
 * <p>
 * The Publication/Subscription system organize {@code Message} publishing and subscription
 * around Topics.
 * </p>
 * <p>
 * The {@code Topic} class represents a <b>unique</b> identifier
 * on which messages will be published on, and subscribers will subscribe to.
 * </p>
 *
 * @author Nadir Belarouci
 * @author katia Amichi
 * @see Message
 * @see fr.sorbonne_u.components.pubsub.interfaces.PublisherService
 * @see fr.sorbonne_u.components.pubsub.interfaces.SubscriberService
 */
public class Topic implements Serializable {

    /**
     * A final topic that hosts will subscribe to on a distributed environment.
     */
    public static final Topic ROOT = new Topic("ROOT");
    /**
     * name of the topic
     */
    private final String name;

    /**
     * Creates a new topic.
     * <p>
     * Note the class offers a factory method  {@link #of} to create a new topic.
     * </p>
     *
     * @param name topic name
     * @see #of(String)
     */
    private Topic(String name) {
        this.name = name;
    }


    /**
     * Creates a new Topic.
     *
     * @param name A {@code String}
     * @return a new topic
     * @throws IllegalArgumentException when name is null or empty, or name == ROOT.
     */
    public static Topic of(String name) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Topic cannot be null or empty.");
        if (name.equals("ROOT"))
            throw new IllegalArgumentException("Topic cannot be equal to ROOT. RESERVED");

        return new Topic(name);

    }

    /**
     * @return the topic's name.
     */
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Topic)) return false;
        Topic topic = (Topic) o;
        return Objects.equals(getName(), topic.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "Topic{" +
                "name='" + name + '\'' +
                '}';
    }


}

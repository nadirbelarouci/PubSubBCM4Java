package fr.sorbonne_u.pubsub;

import java.io.Serializable;
import java.util.Objects;

public class Topic implements Serializable {
    private final String name;

    private Topic(String name) {
        this.name = name;
    }

    public static TopicBuilder newBuilder(String topic) {
        return new TopicBuilder(topic);
    }

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

    public static class TopicBuilder {
        private String name;

        private TopicBuilder(String name) {
            if (name == null || name.isEmpty())
                throw new IllegalArgumentException("Topic cannot be null or empty.");
            this.name = name;
        }


        public Topic build() {
            return new Topic(name);
        }
    }
}

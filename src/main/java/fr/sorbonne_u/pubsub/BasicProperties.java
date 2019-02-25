package fr.sorbonne_u.pubsub;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BasicProperties {
    private Map<String, Object> properties = new ConcurrentHashMap<>();

    public BasicProperties put(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, byte value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, short value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, char value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, int value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, long value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, float value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, double value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, boolean value) {
        properties.put(key, value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasicProperties)) return false;
        BasicProperties that = (BasicProperties) o;
        return Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

    @Override
    public String toString() {
        return "BasicProperties{" +
                "properties=" + properties +
                '}';
    }
}

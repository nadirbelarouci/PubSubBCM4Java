package fr.sorbonne_u.pubsub;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BasicProperties {
    private Map<String, Object> properties = new ConcurrentHashMap<>();

    public BasicProperties put(String key, String value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, Byte value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, Short value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, Character value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, Integer value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, Long value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, Float value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, Double value) {
        properties.put(key, value);
        return this;
    }

    public BasicProperties put(String key, Boolean value) {
        properties.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "BasicProperties{" +
                "properties=" + properties +
                '}';
    }
}

package fr.sorbonne_u.pubsub;

import java.util.Hashtable;
import java.util.Map;

public class BasicProperties {
    private Map<String, Object> properties = new Hashtable<>();

    public Object put(String key, String value) {
        return properties.put(key, value);
    }

    public Object put(String key, Byte value) {
        return properties.put(key, value);
    }

    public Object put(String key, Short value) {
        return properties.put(key, value);
    }

    public Object put(String key, Character value) {
        return properties.put(key, value);
    }

    public Object put(String key, Integer value) {
        return properties.put(key, value);
    }

    public Object put(String key, Long value) {
        return properties.put(key, value);
    }

    public Object put(String key, Float value) {
        return properties.put(key, value);
    }

    public Object put(String key, Double value) {
        return properties.put(key, value);
    }

    public Object put(String key, Boolean value) {
        return properties.put(key, value);
    }

}

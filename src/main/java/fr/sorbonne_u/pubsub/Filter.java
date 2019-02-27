package fr.sorbonne_u.pubsub;

import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class Filter {
    private final ConcurrentHashMap<String, Filterable> filters = new ConcurrentHashMap<>();


    public Filter addFilter(String key, IntPredicate predicate) {
        filters.put(key, predicate);
        return this;
    }

    public Filter addFilter(String key, DoublePredicate predicate) {
        filters.put(key, predicate);
        return this;
    }

    public Filter addFilter(String key, LongPredicate predicate) {
        filters.put(key, predicate);
        return this;
    }

    public Filter addFilter(String key, StringPredicate predicate) {
        filters.put(key, predicate);
        return this;
    }

    public Filter addFilter(String key, BooleanPredicate predicate) {
        filters.put(key, predicate);
        return this;
    }

    public boolean apply(Message message) {
        synchronized (filters) {
//            filters.entrySet()
//                    .parallelStream()
//                    .forEach(e -> message.filter(e.getKey(), e.getValue()));
        }

        return true;
    }


    private interface Filterable {
    }

    private interface IntPredicate extends Filterable, java.util.function.IntPredicate {

    }

    private interface DoublePredicate extends Filterable, java.util.function.DoublePredicate {

    }

    private interface LongPredicate extends Filterable, java.util.function.LongPredicate {

    }

    private interface StringPredicate extends Filterable, Predicate<String> {

    }

    private interface BooleanPredicate extends Filterable, Predicate<Boolean> {

    }

}

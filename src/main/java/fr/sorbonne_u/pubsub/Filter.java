package fr.sorbonne_u.pubsub;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

public class Filter {
    private final ConcurrentHashMap<String, Filterable> filters = new ConcurrentHashMap<>();
    private ForkJoinPool forkJoinPool = new ForkJoinPool(3);

//    public static void main() {
////        new Filter().addFilter("", (int i) -> i > 5);
//    }


    public Filter addFilter(String key, Filterable predicate) {
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
        boolean test(byte i);

        boolean test(short i);

        boolean test(char i);

        boolean test(int i);

        boolean test(long i);

        boolean test(float i);

        boolean test(double i);

    }
}

package fr.sorbonne_u.pubsub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class Filter {

    private List<Predicate<Message>> filters = new ArrayList<>();

    public void add(Predicate<Message>... conditions) {
        filters.addAll(Arrays.asList(conditions));
    }

}

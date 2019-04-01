package fr.sorbonne_u.pubsub.interfaces;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;

import java.util.function.Predicate;

public interface SubscriberService {
    void subscribe(Topic topic);

    void subscribe(Topic topic, Predicate<Message> filter);

    void updateFilter(Topic topic, Predicate<Message> filter);

    void unsubscribe(Topic topic);

    void unsubscribe();
}

package fr.sorbonne_u.pubsub.components;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

public class BrokerTest {


    private Broker broker = Broker.getInstance();
    private List<Observer> observers = new ArrayList<>();

    @Before
    public void setup() {
        observers.add(new ObserverMock("1"));
        observers.add(new ObserverMock("2"));
        observers.add(new ObserverMock("3"));
    }

    @Test
    public void removeingAnExistingTopicShouldRetunTrue() {
        broker.subscribe("Topic", observers.get(0));
        broker.subscribe("Topic", observers.get(1));
        assertTrue(broker.remove("Topic"));
        assertFalse(broker.remove("Topic"));
        assertFalse(broker.isSubscribed("Topic", observers.get(0)));
        assertFalse(broker.isSubscribed("Topic", observers.get(1)));

    }

    @Test
    public void unsubscribeFromAllTopicsShouldReturnTrue() {
        broker.subscribe("Topic1", observers.get(0));
        broker.subscribe("Topic2", observers.get(0));
        assumeTrue(broker.isSubscribed("Topic1", observers.get(0)));
        assumeTrue(broker.isSubscribed("Topic2", observers.get(0)));

        assertTrue(broker.unsubscribe(observers.get(0)));
        assertFalse(broker.unsubscribe(observers.get(0)));
        assertFalse(broker.isSubscribed("Topic1", observers.get(0)));
        assertFalse(broker.isSubscribed("Topic2", observers.get(0)));

    }

    @Test
    public void unsubscribeFromATopicShouldReturnTrue() {
        broker.subscribe("Topic1", observers.get(0));
        broker.subscribe("Topic2", observers.get(0));


        assumeTrue(broker.isSubscribed("Topic1", observers.get(0)));
        assumeTrue(broker.isSubscribed("Topic2", observers.get(0)));

        assertTrue(broker.unsubscribe("Topic1", observers.get(0)));

        assertFalse(broker.isSubscribed("Topic1", observers.get(0)));
        assertTrue(broker.isSubscribed("Topic2", observers.get(0)));

    }

    @Test
    public void subscribeShouldRegisterASubscriberToATopic() {
        broker.subscribe("Topic1", observers.get(0));
        broker.subscribe("Topic2", observers.get(0));
        broker.subscribe("Topic1", observers.get(1));
        broker.subscribe("Topic2", observers.get(1));

        assertTrue(broker.isSubscribed("Topic1", observers.get(0)));
        assertTrue(broker.isSubscribed("Topic2", observers.get(0)));
        assertTrue(broker.isSubscribed("Topic1", observers.get(1)));
        assertTrue(broker.isSubscribed("Topic2", observers.get(1)));

    }

    @Test
    public void addTopic() {
        broker.addTopic("Topic");
        assertTrue(broker.containsTopic("Topic"));
        assertFalse(broker.containsTopic("AnOtherTopic"));
    }

    @Test
    public void publishShouldNotifyTheSubscriber() {

    }

    @Test
    public void isSubscribed() {
        broker.subscribe("Topic", observers.get(0));
        assertTrue(broker.isSubscribed("Topic", observers.get(0)));
        assertFalse(broker.isSubscribed("AnOtherTopic", observers.get(0)));
        assertFalse(broker.isSubscribed("Topic", observers.get(1)));
    }
}
package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class BrokerTest {

    private final static Topic TOPIC1 = Topic.newBuilder("TOPIC1").build();
    private final static Topic TOPIC2 = Topic.newBuilder("TOPIC2").build();
    private final static Topic TOPIC3 = Topic.newBuilder("TOPIC3").build();

    static {

    }

    private List<ObserverMock> observers = new ArrayList<>();
    private Broker broker = Broker.getInstance();

    @Before
    public void setup() {
        observers.add(new ObserverMock("1"));
        observers.add(new ObserverMock("2"));
        observers.add(new ObserverMock("3"));
    }

    @After
    public void tearDown() {
        observers.clear();
        broker.reboot();
    }

    @Test
    public void removingAnExistingTopicShouldUnsubscribeItsSubsAndNotifyThem() {
        broker.subscribe(TOPIC1, observers.get(0));
        broker.subscribe(TOPIC1, observers.get(1));
        assumeTrue(broker.isSubscribed(TOPIC1, observers.get(0)));
        assumeTrue(broker.isSubscribed(TOPIC1, observers.get(1)));
        broker.removeTopic(TOPIC1);
        assertFalse(broker.hasTopic(TOPIC1));
        assertFalse(broker.isSubscribed(TOPIC1, observers.get(0)));
        assertFalse(broker.isSubscribed(TOPIC1, observers.get(1)));
    }

    @Test
    public void unsubscribeASubscriberShouldUnsubscribeItFromAllTopics() {
        broker.subscribe(TOPIC1, observers.get(0));
        broker.subscribe(TOPIC2, observers.get(0));
        assumeTrue(broker.isSubscribed(TOPIC1, observers.get(0)));
        assumeTrue(broker.isSubscribed(TOPIC2, observers.get(0)));
        broker.unsubscribe(observers.get(0));
        assertFalse(broker.isSubscribed(TOPIC1, observers.get(0)));
        assertFalse(broker.isSubscribed(TOPIC2, observers.get(0)));
    }

    @Test
    public void unsubscribeFromATopicShouldUnsubscribeItJustFromThisTopic() {
        broker.subscribe(TOPIC1, observers.get(0));
        broker.subscribe(TOPIC2, observers.get(0));


        assumeTrue(broker.isSubscribed(TOPIC1, observers.get(0)));
        assumeTrue(broker.isSubscribed(TOPIC2, observers.get(0)));

        broker.unsubscribe(TOPIC1, observers.get(0));

        assertFalse(broker.isSubscribed(TOPIC1, observers.get(0)));
        assertTrue(broker.isSubscribed(TOPIC2, observers.get(0)));

    }

    @Test
    public void subscribeASubscriberToATopic() {
        broker.subscribe(TOPIC1, observers.get(0));
        broker.subscribe(TOPIC2, observers.get(0));
        broker.subscribe(TOPIC1, observers.get(1));
        broker.subscribe(TOPIC2, observers.get(1));

        assertTrue(broker.isSubscribed(TOPIC1, observers.get(0)));
        assertTrue(broker.isSubscribed(TOPIC2, observers.get(0)));
        assertTrue(broker.isSubscribed(TOPIC1, observers.get(1)));
        assertTrue(broker.isSubscribed(TOPIC2, observers.get(1)));
    }

    @Test
    public void publishShouldNotifyTheSubscribers() throws Exception {
        observers.forEach(sub -> broker.subscribe(TOPIC1, sub));
        broker.subscribe(TOPIC2, observers.get(1));
        broker.subscribe(TOPIC2, observers.get(2));
        broker.subscribe(TOPIC3, observers.get(0));

//        observers.forEach(sub -> assumeTrue(broker.isSubscribed(TOPIC1, sub)));
//        assumeTrue(broker.hasTopic(TOPIC1));
        broker.publish(Message.newBuilder(TOPIC1).setContent("Hello World Topic1").build())
                .get();
        for (ObserverMock sub : observers) {


            Message message = sub.getMessage();

            assertNotNull(message);
            assertEquals("Hello World Topic1", message.getContent());
        }


        broker.publish(Message.newBuilder(TOPIC2).setContent("Hello World Topic2").build())
                .get();
        for (int i = 1; i <= 2; i++) {
            Message message = observers.get(i).getMessage();
            assertNotNull(message);
            assertEquals("Hello World Topic2", message.getContent());
        }

        Message message = observers.get(0).getMessage();
        assertEquals("Hello World Topic1", message.getContent());
        broker.publish(Message.newBuilder(TOPIC3).setContent("Hello World Topic3").build())
                .get();

        message = observers.get(0).getMessage();
        assertNotNull(message);
        assertEquals("Hello World Topic3", message.getContent());
    }

    @Test
    public void isSubscribedShouldReturnFalseWhenTheTopicDoesNotExistsOrTheSubscriberIsNotSubscribed() {
        broker.subscribe(TOPIC1, observers.get(0));
        assertFalse(broker.isSubscribed(TOPIC2, observers.get(0)));
        assertFalse(broker.isSubscribed(observers.get(1)));
        assertFalse(broker.isSubscribed(TOPIC1, observers.get(1)));
    }

    @Test
    public void isSubscribedShouldReturnTrueWhenTSubscriberIsSubscribed() {
        broker.subscribe(TOPIC1, observers.get(0));
        assertTrue(broker.isSubscribed(TOPIC1, observers.get(0)));
        assertTrue(broker.isSubscribed(observers.get(0)));
        broker.unsubscribe(observers.get(0));
        assertFalse(broker.isSubscribed(TOPIC1, observers.get(0)));
    }


}
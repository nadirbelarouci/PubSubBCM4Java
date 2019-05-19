package fr.sorbonne_u.components.pubsub.components;

import fr.sorbonne_u.components.pubsub.Message;
import fr.sorbonne_u.components.pubsub.Topic;
import fr.sorbonne_u.components.pubsub.interfaces.Subscription;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

public class BrokerTest {

    private final static Topic TOPIC1 = Topic.of("TOPIC1");
    private final static Topic TOPIC2 = Topic.of("TOPIC2");
    private final static Topic TOPIC3 = Topic.of("TOPIC3");


    private List<SubscriptionMock> observers = new ArrayList<>();
    private Broker broker = new Broker(5, 5);

    @Before
    public void setUp() {
        broker = new Broker();
        observers.add(new SubscriptionMock("1"));
        observers.add(new SubscriptionMock("2"));
        observers.add(new SubscriptionMock("3"));
    }

    @After
    public void tearDown() {
        broker = null;
    }

    @Test
    public void removingAnExistingTopicShouldUnsubscribeItsSubsAndNotifyThem() throws ExecutionException, InterruptedException {
        broker.subscribe(observers.get(0), TOPIC1).get();
        broker.subscribe(observers.get(1), TOPIC1).get();
        assumeTrue(broker.isSubscribed(observers.get(0), TOPIC1));
        assumeTrue(broker.isSubscribed(observers.get(1), TOPIC1));
        broker.removeTopic(TOPIC1).get();
        assertFalse(broker.hasTopic(TOPIC1));
        assertFalse(broker.isSubscribed(observers.get(0), TOPIC1));
        assertFalse(broker.isSubscribed(observers.get(1), TOPIC1));
    }

    @Test
    public void unsubscribeASubscriberShouldUnsubscribeItFromAllTopics() throws ExecutionException, InterruptedException {
        broker.subscribe(observers.get(0), TOPIC1).get();
        broker.subscribe(observers.get(0), TOPIC2).get();
        assumeTrue(broker.isSubscribed(observers.get(0), TOPIC1));
        assumeTrue(broker.isSubscribed(observers.get(0), TOPIC2));
        broker.unsubscribe(observers.get(0)).get();
        assertFalse(broker.isSubscribed(observers.get(0), TOPIC1));
        assertFalse(broker.isSubscribed(observers.get(0), TOPIC2));
    }

    @Test
    public void unsubscribeFromATopicShouldUnsubscribeItJustFromThisTopic() throws ExecutionException, InterruptedException {
        broker.subscribe(observers.get(0), TOPIC1).get();
        broker.subscribe(observers.get(0), TOPIC2).get();


        assumeTrue(broker.isSubscribed(observers.get(0), TOPIC1));
        assumeTrue(broker.isSubscribed(observers.get(0), TOPIC2));

        broker.unsubscribe(observers.get(0), TOPIC1).get();

        assertFalse(broker.isSubscribed(observers.get(0), TOPIC1));
        assertTrue(broker.isSubscribed(observers.get(0), TOPIC2));

    }

    @Test
    public void subscribeASubscriberToATopic() throws ExecutionException, InterruptedException {
        broker.subscribe(observers.get(0), TOPIC1).get();
        broker.subscribe(observers.get(0), TOPIC2).get();
        broker.subscribe(observers.get(1), TOPIC1).get();
        broker.subscribe(observers.get(1), TOPIC2).get();

        assertTrue(broker.isSubscribed(observers.get(0), TOPIC1));
        assertTrue(broker.isSubscribed(observers.get(0), TOPIC2));
        assertTrue(broker.isSubscribed(observers.get(1), TOPIC1));
        assertTrue(broker.isSubscribed(observers.get(1), TOPIC2));
    }

    @Test
    public void publishShouldNotifyTheSubscribers() throws Exception {

        for (Subscription sub : observers) {
            broker.subscribe(sub, TOPIC1).get();
        }
        broker.subscribe(observers.get(1), TOPIC2).get();
        broker.subscribe(observers.get(2), TOPIC2).get();
        broker.subscribe(observers.get(0), TOPIC3).get();


        broker.publish(Message.newBuilder(TOPIC1).setContent("Hello World Topic1").build())
                .get();
        for (SubscriptionMock sub : observers) {


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
    public void isSubscribedShouldReturnFalseWhenTheTopicDoesNotExistsOrTheSubscriberIsNotSubscribed() throws ExecutionException, InterruptedException {
        broker.subscribe(observers.get(0), TOPIC1).get();
        assertFalse(broker.isSubscribed(observers.get(0), TOPIC2));
        assertFalse(broker.isSubscribed(observers.get(1)));
        assertFalse(broker.isSubscribed(observers.get(1), TOPIC1));
    }

    @Test
    public void isSubscribedShouldReturnTrueWhenTSubscriberIsSubscribed() throws ExecutionException, InterruptedException {
        broker.subscribe(observers.get(0), TOPIC1).get();
        assertTrue(broker.isSubscribed(observers.get(0), TOPIC1));
        assertTrue(broker.isSubscribed(observers.get(0)));
        broker.unsubscribe(observers.get(0)).get();
        assertFalse(broker.isSubscribed(observers.get(0), TOPIC1));
    }

    public abstract class Yossarian {
        public final boolean isCrazy() {
            return false;
        }
    }
    public class Sub extends Yossarian{

    }


}

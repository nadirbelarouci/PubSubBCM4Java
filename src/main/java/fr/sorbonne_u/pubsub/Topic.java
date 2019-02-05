package fr.sorbonne_u.pubsub;

import fr.sorbonne_u.pubsub.interfaces.SubscriberI;

import java.util.Vector;

public class Topic {
    private String name;
    private Vector<SubscriberI> subscribers;
}

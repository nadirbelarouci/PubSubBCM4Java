/**
 * A publication / subscription communication system is a system for creating,
 * sending, receiving and consuming messages.
 * <p>
 * The main part of the system is the broker who manages the transmission
 * of messages between its clients.
 * Clients can be either:
 * <ul>
 * <li>
 * A {@link fr.sorbonne_u.components.pubsub.components.Publisher} who creates
 * and publishes messages.
 * </li>
 * <li>
 * A {@link fr.sorbonne_u.components.pubsub.components.Subscriber} who subscribes,
 * receives messages, and consumes them.
 * </li>
 * </ul>
 * <p>
 * The publication / subscription model is a model of  multiplicity 1: N,
 * i.e a message published by a {@code Publisher} can be received by N  {@code Subscriber}s.
 * <p>
 * In this model the {@code Publisher}s do not know the {@code Subscriber}s
 * of their messages nor the {@code Subscriber}s know the {@code Publisher}
 * of the messages that they are receiving.
 * <p>
 * The broker is the link between the {@code Publisher}s and the {@code Subscriber}s,
 * it receives the published messages and forwards them to the {@code Subscriber}s.
 * The broker must guarantees the delivery of the messages and
 * the fact that each {@code Subscriber} receives each of the messages that he receives only once.
 * <p>
 * Publication and subscription are organized around topics: A {@code Publisher} posts
 * messages on topics and a {@code Subscriber} subscribes to a
 * topic on which it will receives message.
 * When  {@code Subscriber}s subscribe, they can provide a filter allowing to specify among
 * the messages published on the subject to which they subscribe which interests them.
 *
 * @author Nadir Belarouci
 * @author katia Amichi
 */
package fr.sorbonne_u.components.pubsub;

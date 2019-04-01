package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.MessagePublisher;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class MessageHandlerExecutor extends HandlerExecutor {

    protected MessageHandlerExecutor() {
        super();
    }

    protected MessageHandlerExecutor(ExecutorService executor) {
        super(executor);
    }

    protected CompletableFuture<Void> submit(Message message, Collection<MessagePublisher> observers) {
        return CompletableFuture.runAsync(() -> sendMessage(message, observers), super.executor);
    }

    private void sendMessage(Message message, Collection<MessagePublisher> observers) {
        if (observers != null) {
            observers.stream()
                    .parallel()
                    .filter(obs -> obs.accept(message))
                    .forEach(obs -> obs.sendMessage(message));
        }
    }
}

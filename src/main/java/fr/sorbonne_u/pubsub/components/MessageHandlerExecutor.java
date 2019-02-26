package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Message;
import fr.sorbonne_u.pubsub.interfaces.Observer;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class MessageHandlerExecutor extends HandlerExecutor{

    protected MessageHandlerExecutor() {
        super();

    }

    protected MessageHandlerExecutor(ExecutorService executor) {
        super(executor);
    }

    protected CompletableFuture<Void> submit(Message message, Set<Observer> observers) {
        return CompletableFuture.runAsync(() -> sendMessage(message, observers), super.executor);
    }

    private void sendMessage(Message message, Set<Observer> observers) {
        if (observers != null) {
            observers.forEach(obs -> obs.update(message));
        }
    }
}

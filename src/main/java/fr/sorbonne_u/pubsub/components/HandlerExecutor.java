package fr.sorbonne_u.pubsub.components;

import fr.sorbonne_u.pubsub.Topic;
import fr.sorbonne_u.pubsub.interfaces.Observer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class HandlerExecutor {

    protected ExecutorService executor;

    protected HandlerExecutor() {
        executor = Executors.newCachedThreadPool();
    }

    protected HandlerExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public void shutdown() {
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executor.shutdownNow();
        }
    }


}

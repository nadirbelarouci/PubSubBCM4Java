package fr.sorbonne_u.pubsub.components;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public abstract class HandlerExecutor {

    protected ExecutorService executor;

    protected HandlerExecutor() {
        executor = new ForkJoinPool(10);
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

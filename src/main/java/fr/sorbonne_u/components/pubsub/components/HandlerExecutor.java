package fr.sorbonne_u.components.pubsub.components;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A {@code HandlerExecutor} is an abstraction of handling the requests that a broker will receive.
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see PublisherExecutor
 * @see SubscriberExecutor
 */
public abstract class HandlerExecutor {
    /**
     * An executor service that handles requests.
     */
    private final ExecutorService executor;

    /**
     * Create a defalut executor with parallelism equals to 10.
     */
    protected HandlerExecutor() {
        this(10);
    }

    /**
     * Create an executor with a specific parallelism value.
     *
     * @param parallelism An {@code int} value.
     */
    protected HandlerExecutor(int parallelism) {
        this.executor = Executors.newWorkStealingPool(parallelism);
    }

    /**
     * Shutdown the executor.
     * ShutdownNow after 5 seconds.
     */
    protected void shutdown() {
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
        } finally {
            executor.shutdownNow();
        }
    }

    /**
     * Run asynchronously a runnable block
     *
     * @return A {@code CompletableFuture} of this runnable block.
     */
    protected CompletableFuture<Void> runAsync(Runnable runnable) {
        return CompletableFuture.runAsync(runnable, executor);
    }

}

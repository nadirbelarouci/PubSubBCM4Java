package fr.sorbonne_u.components.pubsub.components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.cvm.AbstractCVM;

import java.util.Objects;

/**
 * A {@code CBuilder} class represents an implementation of the builder design pattern.
 * <p>
 * A {@code CBuilder} class helps creating components and deploying the {@code Component Virtual Machine (CVM)}.
 * All the publication/subscription system components offers a builder,
 * so in order to create one of these components always consider using their builders.
 * </p>
 *
 * @author Nadir Belarouci
 * @author Katia Amichi
 * @see AbstractComponent
 * @see AbstractCVM
 */
public abstract class CBuilder<T extends CBuilder<T>> {
    /**
     * The CVM.
     */
    protected final AbstractCVM cvm;
    /**
     * This component URI.
     */
    protected String reflectionInboundPortURI;

    /**
     * Number of threads of this component.
     */
    protected int nbThreads;

    /**
     * Number of scheduled threads of this component.
     */
    protected int nbSchedulableThreads;

    /**
     * Enable tracing if true.
     */
    private boolean tracing;
    /**
     * Enable logging if true
     */
    private boolean logging;

    /**
     * Creates a new builder.
     *
     * @param cvm An {@code AbstractCVM}
     */
    protected CBuilder(AbstractCVM cvm) {
        this.cvm = Objects.requireNonNull(cvm, "CVM cannot be null");
        logging = true;
        tracing = true;
    }

    /**
     * Set this component URI.
     *
     * @param reflectionInboundPortURI A unique {@code String}
     * @return This builder
     */
    public T setReflectionInboundPortURI(String reflectionInboundPortURI) {
        this.reflectionInboundPortURI = reflectionInboundPortURI;
        return self();
    }


    /**
     * Set logging.
     *
     * @param logging A {@code boolean} value
     * @return This builder
     */
    public T setLogging(boolean logging) {
        this.logging = logging;
        return self();
    }

    /**
     * Set logging.
     *
     * @param tracing A {@code boolean} value
     * @return This builder
     */

    public T setTracing(boolean tracing) {
        this.tracing = tracing;
        return self();
    }

    /**
     * Set the number of threads for this component.
     *
     * @param nbThreads A {@code int} value
     * @return This builder
     */

    public T setNbThreads(int nbThreads) {
        this.nbThreads = nbThreads;
        return self();
    }


    /**
     * Set the number of scheduled threads for this component.
     *
     * @param nbSchedulableThreads A {@code int} value
     * @return This builder
     */

    public T setNbSchedulableThreads(int nbSchedulableThreads) {
        this.nbSchedulableThreads = nbSchedulableThreads;
        return self();
    }

    /**
     * Deploy the component to the CVM, config logging and tracing.
     *
     * @param componentI A component
     */
    protected void deploy(ComponentI componentI) {
        cvm.addDeployedComponent(componentI);
        if (logging)
            componentI.toggleLogging();
        if (tracing)
            componentI.toggleTracing();
    }

    /**
     * Subclasses must override this method to return {@code this}.
     *
     * @return this
     */
    protected abstract T self();

    /**
     * Build the component.
     *
     * @return a new deployed configured component.
     */
    public abstract AbstractComponent build() throws Exception;
}

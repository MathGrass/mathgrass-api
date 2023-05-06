package de.tudresden.inf.st.mathgrass.api.events;

import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Extension of the Guava event bus allowing for storing and getting registered listeners..
 */
@Component
public class CustomEventBus {
    /**
     * The event bus.
     */
    private final EventBus eventBus;

    /**
     * Registered listeners.
     */
    private final Set<Object> listeners;

    /**
     * Constructor.
     *
     * @param eventBus event bus
     */
    public CustomEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        this.listeners = Collections.synchronizedSet(new HashSet<>());
    }

    /**
     * Register a listener.
     *
     * @param listener listener to register
     */
    public void register(Object listener) {
        eventBus.register(listener);
        listeners.add(listener);
    }

    /**
     * Unregister a listener.
     *
     * @param listener listener to unregister
     */
    public void unregister(Object listener) {
        eventBus.unregister(listener);
        listeners.remove(listener);
    }

    /**
     * Get all registered listeners.
     *
     * @return registered listeners
     */
    public Set<Object> getRegisteredListeners() {
        return Collections.unmodifiableSet(listeners);
    }

    /**
     * Post an event.
     *
     * @param event event to post
     */
    public void post(Object event) {
        eventBus.post(event);
    }
}

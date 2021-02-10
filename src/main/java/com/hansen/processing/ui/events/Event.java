package com.hansen.processing.ui.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for events
 * @author Florian Hansen
 *
 * @param <T> event argument class
 */
public class Event<T> {

    private List<EventListener<T>> eventListeners = new ArrayList<>();

    /**
     * Adds a listener to the event
     * @param listener
     */
    public void addListener(EventListener<T> listener) {
        eventListeners.add(listener);
    }

    /**
     * Removes a listener from the event
     * @param listener
     */
    public void removeListener(EventListener<T> listener) {
        eventListeners.remove(listener);
    }

    /**
     * Informs all listeners listening on the event
     * @param sender
     * @param args
     */
    public void invoke(Object sender, T args) {
        for (EventListener<T> listener : eventListeners) {
        	
            try {
				listener.onEvent(sender, args);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
            
        }
    }

}

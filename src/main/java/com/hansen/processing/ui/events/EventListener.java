package com.hansen.processing.ui.events;

/**
 * Interface to listen on events
 * @author Florian Hansen
 *
 * @param <T> event argument class type
 */
public interface EventListener<T> {

	/**
	 * Will be executed when an event is fired
	 * @param sender
	 * @param eventArgs
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
    public void onEvent(Object sender, T eventArgs) throws NoSuchFieldException, IllegalAccessException;

}

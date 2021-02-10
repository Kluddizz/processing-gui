package com.hansen.processing.ui.events;

/**
 * Interface to notify when a property has changed
 * @author 49157
 *
 */
public interface NotifyPropertyChanged {

	/**
	 * Notifies listeners that a property has changed
	 * @param propertyName
	 */
    public void notifyPropertyChanged(String propertyName);

    /**
     * Adds a property changed listener
     * @param listener
     */
    public void addPropertyChangedListener(EventListener<PropertyChangedEventArgs> listener);

}

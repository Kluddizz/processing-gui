package com.hansen.processing.ui.events;

/**
 * Event argument class, which contains information about the property, which has changed
 * @author Florian Hansen
 *
 */
public class PropertyChangedEventArgs {

    private String propertyName;

    public PropertyChangedEventArgs(String propertyName) {
        setPropertyName(propertyName);
    }

    /**
     * @return the property name
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Sets the property name
     * @param propertyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}

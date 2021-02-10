package com.hansen.processing.ui.controller;

import com.hansen.processing.ui.annotations.ControlReference;
import com.hansen.processing.ui.controls.Control;
import com.hansen.processing.ui.events.NotifyPropertyChanged;
import com.hansen.processing.ui.events.EventListener;
import com.hansen.processing.ui.events.PropertyChangedEvent;
import com.hansen.processing.ui.events.PropertyChangedEventArgs;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A Controller can be used to control information flows inside a Processing application and its controls following to the MVC pattern.
 * @author Florian Hansen
 *
 */
public abstract class Controller implements NotifyPropertyChanged {
	
	public Object target;
	
    // this list contains all instantiated controllers to be able to initialize them
    private static List<Controller> controllers = new ArrayList<>();

    /**
     * Static method to get the list of instantiated controllers
     * @return List of all instantiated controllers
     */
    public static List<Controller> getControllers() {
        return controllers;
    }

    /**
     * This method is used to initialize all registered controllers and their annotated attributes
     */
    public static void initControllers() {
        // iterate over every registered controller and initialize it
        for (Controller controller : controllers) {
            // initialize fields of the controller
            for (Field field : controller.getClass().getDeclaredFields()) {

                if (field.isAnnotationPresent(ControlReference.class)) {
                    // check if a field is annotated as control reference and set it up
                    try {

                        // find the control and assign it to the field
                        Control control = Control.getControlById(field.getName());
                        controller.setControl(field.getName(), control);
                    } catch (IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException e) {
                        e.printStackTrace();
                    }

                }

            }

            // call the initialization callback method of every controller, which can be used inside every subclass
            controller.init();
        }
    }
    
    public void initController() {
    	for (Field field : getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(ControlReference.class)) {
                // check if a field is annotated as control reference and set it up
                try {

                    // find the control and assign it to the field
                    Control control = Control.getControlById(field.getName());
                    setControl(field.getName(), control);
                } catch (IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException e) {
                    e.printStackTrace();
                }

            }

        }

        // call the initialization callback method of every controller, which can be used inside every subclass
        init();
    }

    private PropertyChangedEvent propertyChangedEvent = new PropertyChangedEvent();
    
    @Override
    public void notifyPropertyChanged(String propertyName) {
        propertyChangedEvent.invoke(this, new PropertyChangedEventArgs(propertyName));
    }

    @Override
    public void addPropertyChangedListener(EventListener<PropertyChangedEventArgs> listener) {
        propertyChangedEvent.addListener(listener);
    }

    /**
     * Update callback
     */
    public void update() { }

    /**
     * Constructor
     */
    public Controller() {
        controllers.add(this);
    }

    /**
     * Setter method of control
     * @param control The control
     * @throws SecurityException 
     * @throws NoSuchFieldException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    public void setControl(String fieldName, Control control) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<?> cls = getClass();
        
    	Field field = cls.getDeclaredField(fieldName);
    	field.setAccessible(true);
    	field.set(this, control);
    }

    /**
     * This initialization callback is called, whenever initControllers() was called
     * @param control The control of this instance
     */
    protected abstract void init();

}

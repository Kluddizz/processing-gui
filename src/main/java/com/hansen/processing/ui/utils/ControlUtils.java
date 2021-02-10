package com.hansen.processing.ui.utils;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.net.URL;

import javax.xml.bind.JAXBException;

import com.hansen.processing.ui.application.ProcessingApplication;
import com.hansen.processing.ui.controls.Alignment;
import com.hansen.processing.ui.controls.CheckBox;
import com.hansen.processing.ui.controls.Control;
import com.hansen.processing.ui.controls.Label;
import com.hansen.processing.ui.controls.Orientation;
import com.hansen.processing.ui.controls.StackPanel;
import com.hansen.processing.ui.controls.TextBox;
import com.hansen.processing.ui.controls.TextBoxType;
import com.hansen.processing.ui.controls.Thickness;
import com.hansen.processing.ui.xml.XMLLoader;

public class ControlUtils {

    /**
     * Loads a control tree from a resource.
     * @param resourceName Name of the resource in the resource folder
     * @return Control tree
     */
    public static Control load(String resourceName) {
    	Control control = null;
    	
    	try {
    		control = XMLLoader.load(ProcessingApplication.class.getResource("/" + resourceName));
		} catch (JAXBException | FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	return control;
    }
    
    /**
     * Loads a control tree from a resource.
     * @param resource Resource of the resource in the resource folder
     * @return Control tree
     */
    public static Control load(URL resource) {
    	Control control = null;
    	
    	try {
    		control = XMLLoader.load(resource);
		} catch (JAXBException | FileNotFoundException e) { }
    	
    	return control;
    }
    
    /**
     * Loads a control from XML resource and assign its controller a target
     * @param resourceName
     * @param controllerTarget
     * @return
     */
    public static Control load(String resourceName, Object controllerTarget) {
    	Control control = null;
    	
    	try {
    		control = XMLLoader.load(ProcessingApplication.class.getResource("/" + resourceName), controllerTarget);
		} catch (JAXBException | FileNotFoundException e) { }
    	
    	return control;
    }

	
    /**
     * Creates a new control based on an object. If there is an XML resource inside
     * the resource folder with the same name, then it will return the rendered
     * version of it. Otherwise the result will be an automatic rendered control
     * based on the objects fields.
     * 
     * <p>
     * 
     * <b>Example for resource:</b>
     * 
     * <p>
     * 
     * Let's assume, that you want to create a control for an instance of the class {@code MySpecialClass}. Then you can define the control based on it with a resource called {@code MySpecialClass.xml} inside the resource folder.
     * @param object
     * @return
     */
	public static Control createObjectControl(Object object) {
		// create container element to store all subcomponents of the object component
    	StackPanel panel = new StackPanel();
    	panel.setStretchHorizontal(true);
    	panel.setBackgroundColor("#ffe6e6e6");
    	
    	// create title for the object control
    	String title = StringUtils.decamel(object.getClass().getSimpleName());
    	Label titleLabel = new Label();
    	titleLabel.setStretchHorizontal(true);
    	titleLabel.setBackgroundColor("#ffd6d6d6");
    	titleLabel.setPadding(new Thickness(15, 5));
    	titleLabel.setText(title);
    	panel.addChild(titleLabel);
    	
    	// try to load a resource with the same name as the object
    	Control control = load(object.getClass().getSimpleName() + ".xml", object);
    	
    	if (control != null) {
    		// the resource exists, we're done. Put the loaded control into the container
    		panel.addChild(control);
    	} else {
    		// create for each field its own control
	    	for (Field field : object.getClass().getFields()) {
	    		StackPanel row = new StackPanel();
	    		row.setOrientation(Orientation.HORIZONTAL);
	    		
				Label fieldLabel = new Label();
				fieldLabel.setText(StringUtils.decamel(field.getName()));
				fieldLabel.setTextAlignment(Alignment.LEFT);
				fieldLabel.setWidth(100);
				fieldLabel.setHeight(26);
				fieldLabel.setPadding(new Thickness(15, 0));
				row.addChild(fieldLabel);
				
				Class<?> fieldType = field.getType();
				
				if (fieldType.isAssignableFrom(Float.TYPE)) {
					TextBox textBox = new TextBox();
					textBox.setType(TextBoxType.NUMBER);
					textBox.setVerticalAlignment(Alignment.CENTER);
					textBox.setSize(100, 20);
					textBox.setBorderRadius(2.0f);
					textBox.setTextAlignment(Alignment.CENTER);
					
					try {
						textBox.setText(String.valueOf(field.get(object)));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					
					textBox.addTextChangedListener((c, oldText, newText) -> {
						try {
							field.set(object, Float.parseFloat(newText));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							textBox.setText(oldText);
						}
					});
					row.addChild(textBox);
				} else if (fieldType.isAssignableFrom(Boolean.TYPE)) {
					CheckBox checkBox = new CheckBox();
					checkBox.setVerticalAlignment(Alignment.CENTER);
					
					try {
						checkBox.setChecked((boolean) field.get(object));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					
					row.addChild(checkBox);
				}
				
				panel.addChild(row);
	    	}
    	}
    	
    	return panel;
    }
	
}

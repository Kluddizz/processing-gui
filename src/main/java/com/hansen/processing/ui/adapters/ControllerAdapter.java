package com.hansen.processing.ui.adapters;

import com.hansen.processing.ui.controller.Controller;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class converts a String to a Controller while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class ControllerAdapter extends XmlAdapter<String, Controller> {

    @SuppressWarnings("unchecked")
	  @Override
    public Controller unmarshal(String s) throws Exception {
    	// load class type instance of referenced controller class
        Class<Controller> controllerClass = (Class<Controller>) Class.forName(s);
        
        // return new instance of controller class
        return controllerClass.getDeclaredConstructor().newInstance();
    }

    @Override
    public String marshal(Controller controller) throws Exception {
        return null;
    }

}

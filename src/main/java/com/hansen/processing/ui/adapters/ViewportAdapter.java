package com.hansen.processing.ui.adapters;

import com.hansen.processing.ui.controls.Viewport;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class converts a String to a Viewport while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class ViewportAdapter extends XmlAdapter<String, Viewport> {

    @SuppressWarnings("unchecked")
	@Override
    public Viewport unmarshal(String s) throws Exception {
    	Class<Viewport> sceneClass = null;
    	
    	try {
    		sceneClass = (Class<Viewport>) Class.forName(s);    		
    	} catch (ClassNotFoundException e) {
    		System.err.println("Could not find class '" + s + "'");
    	}
    	
        return sceneClass.getDeclaredConstructor().newInstance();
    }

    @Override
    public String marshal(Viewport scene) throws Exception {
        return scene.getClass().getSimpleName();
    }

}

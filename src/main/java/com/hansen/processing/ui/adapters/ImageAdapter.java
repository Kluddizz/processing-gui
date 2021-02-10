package com.hansen.processing.ui.adapters;

import com.hansen.processing.ui.singleton.Context;
import processing.core.PImage;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.net.URL;

/**
 * This class converts a String to a PImage while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class ImageAdapter extends XmlAdapter<String, PImage> {

    @Override
    public PImage unmarshal(String s) throws Exception {
    	// search for image resource
        URL resource = getClass().getResource(s);
        
        // return new instance of the resource as PImage
        return Context.getInstance().getApp().loadImage(resource.getFile());
    }

    @Override
    public String marshal(PImage pImage) throws Exception {
        return null;
    }

}

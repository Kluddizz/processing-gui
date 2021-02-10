package com.hansen.processing.ui.adapters;

import com.hansen.processing.ui.controls.Orientation;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class converts a String to an Orientation while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class OrientationAdapter extends XmlAdapter<String, Orientation> {

    @Override
    public Orientation unmarshal(String s) throws Exception {
    	// get the orientation type based on the string
        return Orientation.valueOf(s.toUpperCase());
    }

    @Override
    public String marshal(Orientation orientation) throws Exception {
        return orientation.toString().toUpperCase();
    }
}

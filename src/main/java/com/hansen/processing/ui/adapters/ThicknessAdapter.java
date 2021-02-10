package com.hansen.processing.ui.adapters;

import com.hansen.processing.ui.controls.Thickness;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class converts a String to a Thickness while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class ThicknessAdapter extends XmlAdapter<String, Thickness> {

    @Override
    public Thickness unmarshal(String s) throws Exception {
    	// create default thickness with only zeros as values for top, left, bottom, right
        Thickness thickness = new Thickness();
        
        // remove all spaces and comma-separate the result
        String[] parts = s.replace(" ", "").split(",");
        
        // convert the string array to a float array
        float[] values = new float[parts.length];

        for (int i = 0; i < parts.length; i++) {
            values[i] = Float.parseFloat(parts[i]);
        }

        // check which combination of value representation was chosen and recreate the thickness instance
        if (values.length == 4) {
            thickness = new Thickness(values[0], values[1], values[2], values[3]);
        } else if (values.length == 2) {
            thickness = new Thickness(values[0], values[1]);
        } else if (values.length == 1) {
            thickness = new Thickness(values[0]);
        }

        return thickness;
    }

    @Override
    public String marshal(Thickness thickness) throws Exception {
        return thickness.toString();
    }

}

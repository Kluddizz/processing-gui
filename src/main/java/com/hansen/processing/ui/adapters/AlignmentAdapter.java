package com.hansen.processing.ui.adapters;

import com.hansen.processing.ui.controls.Alignment;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class converts a String to an Alignment while unmarshalling / marshalling.
 * @author Florian Hansen
 * 
 */
public class AlignmentAdapter extends XmlAdapter<String, Alignment> {

    @Override
    public Alignment unmarshal(String s) throws Exception {
        return Alignment.valueOf(s.toUpperCase());
    }

    @Override
    public String marshal(Alignment alignment) throws Exception {
        return alignment.toString().toUpperCase();
    }

}

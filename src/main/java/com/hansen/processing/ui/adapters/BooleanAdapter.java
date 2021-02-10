package com.hansen.processing.ui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class converts a String to a Boolean while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class BooleanAdapter extends XmlAdapter<String, Boolean> {
    @Override
    public Boolean unmarshal(String s) throws Exception {
        return s.equalsIgnoreCase("TRUE");
    }

    @Override
    public String marshal(Boolean aBoolean) throws Exception {
        if (aBoolean) {
            return "True";
        } else {
            return "False";
        }
    }
}

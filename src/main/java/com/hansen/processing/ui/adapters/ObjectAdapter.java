package com.hansen.processing.ui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class converts a String to a Object while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class ObjectAdapter extends XmlAdapter<String, Object> {
    @Override
    public Object unmarshal(String s) throws Exception {
    	// return new instance of the objects type
        return Class.forName(s).getDeclaredConstructor().newInstance();
    }

    @Override
    public String marshal(Object o) throws Exception {
        return o.getClass().getName();
    }
}

package com.hansen.processing.ui.adapters;

import com.hansen.processing.ui.controls.Control;
import com.hansen.processing.ui.xml.XMLLoader;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * This class converts a String to a Control while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class ControlAdapter extends XmlAdapter<String, Control> {

    @Override
    public Control unmarshal(String s) throws Exception {
        return XMLLoader.load(getClass().getResource(s));
    }

    @Override
    public String marshal(Control control) throws Exception {
        return null;
    }

}

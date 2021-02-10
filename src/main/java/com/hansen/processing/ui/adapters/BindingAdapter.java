package com.hansen.processing.ui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.lang.reflect.Field;

/**
 * This class converts a String to a Field while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class BindingAdapter extends XmlAdapter<String, Field> {

    private Object context;

    public BindingAdapter(Object context) {
        this.context = context;
    }

    @Override
    public Field unmarshal(String s) throws Exception {
        String[] properties = s.split(".");

        Object object = context;
        Field field = null;

        // search for a property inside context
        for (int i = 0; i < properties.length; i++) {
            field = object.getClass().getField(properties[i]);
            object = field.get(object);
        }

        return field;
    }

    @Override
    public String marshal(Field field) throws Exception {
        return null;
    }

}

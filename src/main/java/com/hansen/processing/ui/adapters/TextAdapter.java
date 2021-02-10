package com.hansen.processing.ui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class converts a String to a String without brackets while unmarshalling / marshalling.
 * @author Florian Hansen
 *
 */
public class TextAdapter extends XmlAdapter<String, String> {

    @Override
    public String unmarshal(String s) throws Exception {
    	// create pattern to find all strings inside brackets
        Pattern p = Pattern.compile("\\{(.*?)\\}");
        
        // match it
        Matcher m = p.matcher(s);

        if (m.find()) {
        	// the second group is the one we are looking for
            return m.group(1);
        }

        return s;
    }

    @Override
    public String marshal(String s) throws Exception {
        return null;
    }

}

package com.hansen.processing.ui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.hansen.processing.ui.controls.TextBoxType;

/**
 * This class converts a String to a TextBoxType while unmarshalling and marshalling.
 * @author Florian Hansen
 *
 */
public class TextBoxTypeAdapter extends XmlAdapter<String, TextBoxType> {

	@Override
	public TextBoxType unmarshal(String v) throws Exception {
		// return the textbox type based on the string
		return TextBoxType.valueOf(v.toUpperCase());
	}

	@Override
	public String marshal(TextBoxType v) throws Exception {
		return v.toString().toUpperCase();
	}

}

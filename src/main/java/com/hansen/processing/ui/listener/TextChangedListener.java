package com.hansen.processing.ui.listener;

import com.hansen.processing.ui.controls.TextControl;

/**
 * Interface to listen to text changed events of text based controls
 * @author Florian Hansen
 *
 */
public interface TextChangedListener {

	/**
	 * A text has changed
	 * @param control
	 * @param oldText
	 * @param newText
	 */
    public void onTextChange(TextControl control, String oldText, String newText);

}

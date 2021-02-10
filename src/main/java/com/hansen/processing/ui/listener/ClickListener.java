package com.hansen.processing.ui.listener;

import com.hansen.processing.ui.controls.Control;

/**
 * Interface to listen to click events
 * @author Florian Hansen
 *
 */
public interface ClickListener {

	/**
	 * A click happened
	 * @param control
	 */
    public void onClick(Control control);

}

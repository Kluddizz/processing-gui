package com.hansen.processing.ui.application;

/**
 * Interface for listening on resize events.
 * @author Florian Hansen
 *
 */
public interface WindowResizeListener {

	/**
	 * Will be executed whenever the size of the window has changed.
	 * @param width The new width of the window
	 * @param height The new height of the window
	 */
    public void windowResize(float width, float height);

}

package com.hansen.processing.ui.listener;

/**
 * Interface to listen to resize events of controls
 * @author Florian Hansen
 *
 */
public interface ResizeListener {

	/**
	 * A resize happened
	 * @param width
	 * @param height
	 */
    public void onControlResize(float width, float height);

}

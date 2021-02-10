package com.hansen.processing.ui.application;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

/**
 * The WindowListener can be used to handle window specific events. Especially it is used to react on resize events, because Processing doesn't support it by default.
 * @author Florian Hansen
 *
 */
public class WindowListener {

    private float currenWidth;
    private float currentHeight;

    private PApplet app;
    private List<WindowResizeListener> resizeListeners;

    public WindowListener(PApplet app) {
        this.app = app;
        this.resizeListeners = new ArrayList<>();
        
        // make sure, that the "pre" method is executed before rendering the actual frame
        app.registerMethod("pre", this);
    }

    /**
     * This method is used by the Processing application to check whenever the size of the window has changed.
     */
    public void pre() {
    	boolean sizeChanged = app.width != currenWidth || app.height != currentHeight;
    	
        if (sizeChanged) {
        	// update the current size of the window
            currenWidth = app.width;
            currentHeight = app.height;

            // execute every resize listener
            for (WindowResizeListener resizeListener : resizeListeners) {
                resizeListener.windowResize(currenWidth, currentHeight);
            }
        }
    }

    /**
     * Adds a listener to the resize event
     * @param resizeListener
     */
    public void addListener(WindowResizeListener resizeListener) {
        resizeListeners.add(resizeListener);
    }
    
    /**
     * Removes a listener from the resize event
     * @param resizeListener
     */
    public void removeListener(WindowResizeListener resizeListener) {
    	resizeListeners.remove(resizeListener);
    }

}

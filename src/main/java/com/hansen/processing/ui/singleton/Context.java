package com.hansen.processing.ui.singleton;

import processing.core.PApplet;

/**
 * Singleton class to store information about the current renderer and processing application
 * @author Florian Hansen
 *
 */
public class Context {

    private static Context instance;

    private PApplet app;
    private String renderer;

    /**
     * @return the context instance
     */
    public static Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }

        return instance;
    }

    /**
     * @return the app
     */
    public PApplet getApp() {
        return app;
    }

    /**
     * Sets the app of this context
     * @param app
     */
    public void setApp(PApplet app) {
        this.app = app;
    }
    
    /**
     * @return the renderer
     */
    public String getRenderer() {
		return renderer;
	}
    
    /**
     * Sets the renderer
     * @param renderer
     */
    public void setRenderer(String renderer) {
		this.renderer = renderer;
	}
}

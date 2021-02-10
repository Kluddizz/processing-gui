package com.hansen.processing.ui.application;

import com.hansen.processing.ui.controller.Controller;
import com.hansen.processing.ui.controls.Control;
import com.hansen.processing.ui.singleton.Context;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * This class is used to handle applications and their events based on Processing. It is the base class of every application built with this framework.
 * @author Florian Hansen
 *
 */
public abstract class ProcessingApplication extends PApplet implements WindowResizeListener {
	
	private WindowListener windowListener;
    private Control rootControl;
    
    protected abstract void init();
    protected abstract Control initControl();
    
    /**
     * Starts a new ProcessingApplication
     * @param <T>
     * @param application
     */
    protected static <T extends ProcessingApplication> void startApplication(Class<T> application) {
    	startApplication(application, P3D);
    }
    
    /**
     * Starts a new ProcessingApplication with a specific renderer
     * @param <T>
     * @param application
     */
    protected static <T extends ProcessingApplication> void startApplication(Class<T> application, String renderer) {
    	Context.getInstance().setRenderer(renderer);
    	PApplet.main(application);
    }
    
    @Override
    public void settings() {
    	String renderer = Context.getInstance().getRenderer();
        size(1920, 1080, renderer);
    }

    @Override
    public void setup() {
    	// initialize singleton and set the app, which is currently running, to this
        Context.getInstance().setApp(this);
        
        // create a new WindowListener to start receiving resize events, which are not supported by Processing itself
        windowListener = new WindowListener(this);
        windowListener.addListener(this);

        // run an abstract method to initialize the tree structure of controls
        rootControl = initControl();

        if (rootControl != null) {
        	// initialize all controllers
        	Controller.initControllers();
        	
        	// initialize the root control and make it filling the window
        	rootControl.initControl(rootControl.getController());
        	rootControl.setSize(width, height);  
        }
        
        // execute the initialization callback function
    	init();
    }

    @Override
    public void draw() {
        background(200);

        if (rootControl != null) {
        	for (Controller controller : Controller.getControllers()) {
        		controller.update();
        	}
        	
            rootControl.drawControl(getGraphics());
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {
    	if (rootControl != null) {
    		rootControl.handleMouseMoved(event);    		
    	}
    }
    
    @Override
    public void mouseClicked(MouseEvent event) {
    	if (rootControl != null) {
    		rootControl.handleMouseClicked(event);
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
    	if (rootControl != null) {
    		rootControl.handleMousePressed(event);
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
    	if (rootControl != null) {
    		rootControl.handleMouseReleased(event);
        }
    }
    
    @Override
    public void mouseWheel(MouseEvent event) {
    	if (rootControl != null) {
    		rootControl.handleMouseWheel(event);
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent event) {
    	if (rootControl != null) {
    		rootControl.handleMouseDrag(event);
    	}
    }

    @Override
    public void keyPressed(KeyEvent event) {
    	if (rootControl != null) {
    		rootControl.handleKeyPressed(event);
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
    	if (rootControl != null) {
    		rootControl.handleKeyReleased(event);
        }
    }

    @Override
    public void windowResize(float width, float height) {
    	if (rootControl != null) {
	        rootControl.setWidth(width);
	        rootControl.setHeight(height);
        }
    }

}

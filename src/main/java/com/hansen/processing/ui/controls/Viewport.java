package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.singleton.Context;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

/**
 * A Viewport renders objects in separate sections in the user interface.
 * @author Florian Hansen
 *
 */
public abstract class Viewport implements PConstants {

	protected String renderer;
    protected PApplet app;
    protected PGraphics graphics;
    protected float width;
    protected float height;

    public Viewport() {
    	renderer = Context.getInstance().getRenderer();
        app = Context.getInstance().getApp();
        graphics = app.createGraphics((int) width, (int) height, renderer);
        setup();
    }

    /**
     * Renders the viewport
     */
    public void drawViewport() {
        if (graphics.width > 0 && graphics.height > 0) {
            graphics.beginDraw();
            draw();
            graphics.endDraw();
            app.image(graphics, 0, 0);
        }
    }

    /**
     * @return the width
     */
    public float getWidth() {
        return width;
    }

    /**
     * @return the height
     */
    public float getHeight() {
        return height;
    }
    
    /**
     * @return the app
     */
    public PApplet getApp() {
		return app;
	}
    
    /**
     * @return the draw context
     */
    public PGraphics getGraphics() {
		return graphics;
	}

    /**
     * Sets the width of the viewport
     * @param width
     */
    public void setWidth(float width) {
        this.width = width;
        graphics = app.createGraphics((int) width, (int) height, renderer);
    }

    /**
     * Sets the height of the viewport
     * @param height
     */
    public void setHeight(float height) {
        this.height = height;
        graphics = app.createGraphics((int) width, (int) height, renderer);
    }

    public void setup() {
	}

    public void draw() {
	}

    public void update() {
	}

    public void mouseMoved(MouseEvent event) {
	}

    public void mouseClicked(MouseEvent event) {
	}

    public void resize(float width, float height) {
	}
    
    public void keyPressed(KeyEvent event) {
	}

	public void keyReleased(KeyEvent event) {
	}

	public void mouseDragged() {
	}

	public void mouseWheel(MouseEvent event) {
	}

	public void mousePressed() {
	}

	public void mouseReleased() {
	}

}

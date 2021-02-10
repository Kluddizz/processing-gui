package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.adapters.ViewportAdapter;

import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The application control is able to render custom viewports and start / pause the rendering process.
 * @author Florian Hansen
 */
@XmlRootElement(name = "ViewportContainer")
@XmlAccessorType(XmlAccessType.NONE)
public class ViewportContainer extends Control {

    private Viewport viewport;
    private boolean running;

    /**
     * Getter method of sketch
     * @return The viewport
     */
    @XmlAttribute(name = "Viewport")
    @XmlJavaTypeAdapter(ViewportAdapter.class)
    public Viewport getViewport() {
        return viewport;
    }

    /**
     * Setter method of sketch
     * @param viewport The viewport
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
        this.viewport.width = width;
        this.viewport.height = height;
    }

    /**
     * Indicates whether the viewport is running. If it is not running, the update process will not be executed.
     * @return If the viewport is running (updating)
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Setter method of the running state of the viewport
     * @param running The running state
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    protected void draw(PGraphics g) {
        // updating the viewport
        if (isRunning()) {
            viewport.update();
        }

        // rendering the viewport
        if (viewport != null) {
            viewport.drawViewport();
        }
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        viewport.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        viewport.setHeight(height);
    }

    @Override
    protected void click(MouseEvent event) {
        viewport.mouseClicked(event);
    }

    @Override
    public void onControlResize(float width, float height) {
        super.onControlResize(width, height);

        viewport.resize(getWidth(), getHeight());
    }
    
    @Override
    protected void keyPressed(KeyEvent event) {
    	viewport.keyPressed(event);
    }
    
    @Override
    protected void keyReleased(KeyEvent event) {
    	viewport.keyReleased(event);
    }
    
    @Override
    protected void drag(PVector dragDirection, boolean precise) {
    	viewport.mouseDragged();
    }
    
    @Override
    public void mouseWheel(MouseEvent event) {
    	viewport.mouseWheel(event);
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
    	viewport.mousePressed();
    }
    
    @Override
    public void mouseReleased(MouseEvent event) {
    	viewport.mouseReleased();
    }
}

package com.hansen.processing.ui.controls;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.hansen.processing.ui.singleton.Context;
import com.hansen.processing.ui.utils.ColorUtils;

import processing.core.PGraphics;
import processing.event.MouseEvent;

/**
 * The ScrollViewer makes it possible to scroll through its children,
 * if the sum of their heights is greater than the elements height.
 * 
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "ScrollViewer")
public class ScrollViewer extends Control {

	private PGraphics viewport = null;
	private float contentHeight = 0;
	private float scrollBarWidth = 5;
	private float scrollBarHeight = 0;
	private String scrollBarColor = "#50333333";
	private float scrollTop = 0;
	private float scrollStep = 15.0f;
	
	/**
	 * @return the scrollBarWidth
	 */
	@XmlAttribute(name = "ScrollBarWidth")
	public float getScrollBarWidth() {
		return scrollBarWidth;
	}

	/**
	 * @param scrollBarWidth the scrollBarWidth to set
	 */
	public void setScrollBarWidth(float scrollBarWidth) {
		this.scrollBarWidth = scrollBarWidth;
	}

	/**
	 * @return the scrollBarColor
	 */
	@XmlAttribute(name = "ScrollBarColor")
	public String getScrollBarColor() {
		return scrollBarColor;
	}

	/**
	 * @param scrollBarColor the scrollBarColor to set
	 */
	public void setScrollBarColor(String scrollBarColor) {
		this.scrollBarColor = scrollBarColor;
	}

	/**
	 * @return the scrollStep
	 */
	@XmlAttribute(name = "ScrollStep")
	public float getScrollStep() {
		return scrollStep;
	}

	/**
	 * @param scrollStep the scrollStep to set
	 */
	public void setScrollStep(float scrollStep) {
		this.scrollStep = scrollStep;
	}

	public ScrollViewer() {
		super();
		
		createViewport();
	}
	
	@Override
	protected void drawChildren(PGraphics g) {
		if (viewport.width > 0 && viewport.height > 0) {
			// draw children inside the viewport of the scroll viewer
			viewport.beginDraw();
			viewport.clear();
			
			// translate the childrens positions based on the scroll value
			viewport.translate(0, -scrollTop, 0);
			
			for (Control child : children) {
				child.drawControl(viewport);
			}
			
			viewport.endDraw();
			app.image(viewport, 0,  0);
			
			// draw scroll bar on top of the viewer
			drawScrollBar(g);
		}
	}
	
	@Override
	public void setWidth(float width) {
		super.setWidth(width);
		
		// update view
		createViewport();
		recalculateContentHeight();
		recalculateScrollBar();
	}
	
	@Override
	public void setHeight(float height) {
		super.setHeight(height);
		
		// update view
		createViewport();
		recalculateContentHeight();
		recalculateScrollBar();
	}
	
	@Override
	public void addChild(Control child) {
		super.addChild(child);
		
		// update view
		recalculateContentHeight();
		recalculateScrollBar();
	}
	
	@Override
	protected void mouseWheel(MouseEvent event) {
		if (isMouseOver) {
			
			if (event.getCount() > 0 && scrollTop < contentHeight - getHeight())
				scrollTop += Math.min(contentHeight - scrollTop - getHeight(), scrollStep);
			else if (event.getCount() < 0 && scrollTop > 0)
				scrollTop -= Math.min(scrollTop, scrollStep);
			
		}
	}
	
	/**
	 * Draws the scroll bar
	 * 
	 * @param g The graphics instance
	 */
	private void drawScrollBar(PGraphics g) {
		// calculate the position of the scroll bar
		float x = getWidth() - scrollBarWidth;
		float y = scrollTop / contentHeight * getHeight();
		
		// draw the bar on the right side of the viewer
		g.pushMatrix();
		g.fill(ColorUtils.colorFromString(scrollBarColor));
		g.rect(x, y, scrollBarWidth, scrollBarHeight);
		g.popMatrix();
	}
	
	/**
	 * Calculates the content height, which is the sum of all child heights.
	 */
	private void recalculateContentHeight() {
		contentHeight = 0.0f;
		
		for (Control child : children) {
			contentHeight += child.getOffsetHeight();
		}
	}
	
	/**
	 * Calculates the scroll bar bounds
	 */
	private void recalculateScrollBar() {
		if (contentHeight > 0 && contentHeight > getHeight()) {
			float ratio = getHeight() / contentHeight;
			scrollBarHeight = getHeight() * ratio;
		}
	}
	
	/**
	 * Creates a new instance of the viewport, which scrolls over the children of this element
	 */
	private void createViewport() {
		String renderer = Context.getInstance().getRenderer();
		viewport = app.createGraphics((int) getWidth(), (int) getHeight(), renderer);
	}
	
}

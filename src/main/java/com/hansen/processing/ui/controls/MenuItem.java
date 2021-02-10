package com.hansen.processing.ui.controls;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.hansen.processing.ui.utils.ColorUtils;

import processing.core.PGraphics;

/**
 * Represents an item
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "MenuItem")
public class MenuItem extends TextControl {
	
	private String hoverColor = "#20ffffff";
	
	/**
	 * @return color when hovered
	 */
	@XmlAttribute(name = "HoverColor")
	public String getHoverColor() {
		return hoverColor;
	}
	
	/**
	 * Sets hover color
	 * @param hoverColor
	 */
	public void setHoverColor(String hoverColor) {
		this.hoverColor = hoverColor;
	}

    @Override
    protected void draw(PGraphics g) {
        if (isMouseOver) {
            g.fill(ColorUtils.colorFromString(hoverColor));
            g.rect(-getPadding().getLeft(), -getPadding().getTop(), getOffsetWidth(), getOffsetHeight());
        }

        super.draw(g);
    }

    @Override
    protected void hoverEnter() {
        app.cursor(HAND);
    }

    @Override
    protected void hoverLeave() {
        app.cursor(ARROW);
    }

}

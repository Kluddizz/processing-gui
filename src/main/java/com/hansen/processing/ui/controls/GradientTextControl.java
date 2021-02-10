package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.utils.ColorUtils;
import com.hansen.processing.ui.utils.ImageUtils;

import processing.core.PGraphics;
import processing.core.PImage;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Base class for all text element with linear gradient background
 * @author Florian Hansen
 *
 */
public abstract class GradientTextControl extends TextControl {

    protected PImage background;
    protected PImage backgroundHover;

    private float borderRadius;
    private String colorTop;
    private String colorBottom;
    private String colorTopHover;
    private String colorBottomHover;

    public GradientTextControl() {
        super();
    }

    @Override
    protected void draw(PGraphics g) {
        if (!isMouseOver) {
            g.image(background, -getPadding().getLeft(), -getPadding().getTop());
        } else {
            g.image(backgroundHover, -getPadding().getLeft(), -getPadding().getTop());
        }

        super.draw(g);
    }

    /**
     * @return border radius
     */
    @XmlAttribute(name = "BorderRadius")
    public float getBorderRadius() {
        return borderRadius;
    }

    /**
     * @return top color of linear gradient
     */
    @XmlAttribute(name = "ColorTop")
    public String getColorTop() {
        return colorTop;
    }

    /**
     * @return bottom color of linear gradient
     */
    @XmlAttribute(name = "ColorBottom")
    public String getColorBottom() {
        return colorBottom;
    }

    /**
     * @return top color of linear gradient when hovered
     */
    @XmlAttribute(name = "ColorHoverTop")
    public String getColorTopHover() {
        return colorTopHover;
    }

    /**
     * @return bottom color of linear gradient when hovered
     */
    @XmlAttribute(name = "ColorHoverBottom")
    public String getColorBottomHover() {
        return colorBottomHover;
    }

    /**
     * Sets the border radius
     * @param borderRadius
     */
    public void setBorderRadius(float borderRadius) {
        this.borderRadius = borderRadius;
        update();
    }

    /**
     * Sets the top color of the gradient
     * @param colorTop
     */
    public void setColorTop(String colorTop) {
        this.colorTop = colorTop;
        update();
    }

    /**
     * Sets the bottom color of the gradient
     * @param colorBottom
     */
    public void setColorBottom(String colorBottom) {
        this.colorBottom = colorBottom;
        update();
    }

    /**
     * Sets the top color of the gradient when hovered
     * @param colorTopHover
     */
    public void setColorTopHover(String colorTopHover) {
        this.colorTopHover = colorTopHover;
        update();
    }

    /**
     * Sets the bottom color of the gradient when hovered
     * @param colorTopHover
     */
    public void setColorBottomHover(String colorBottomHover) {
        this.colorBottomHover = colorBottomHover;
        update();
    }

    @Override
    protected void update() {
        super.update();

        if (getOffsetWidth() > 0 && getOffsetHeight() > 0) {
            int cT = ColorUtils.colorFromString(colorTop);
            int cB = ColorUtils.colorFromString(colorBottom);
            background = ImageUtils.generateGradient(app, cT, cB, (int) getOffsetWidth(), (int) getOffsetHeight());
            background.mask(ImageUtils.generateMask(app, (int) getOffsetWidth(), (int) getOffsetHeight(), (int) borderRadius));

            int cTHover = ColorUtils.colorFromString(colorTopHover);
            int cBHover = ColorUtils.colorFromString(colorBottomHover);
            backgroundHover = ImageUtils.generateGradient(app, cTHover, cBHover, (int) getOffsetWidth(), (int) getOffsetHeight());
            backgroundHover.mask(ImageUtils.generateMask(app, (int) getOffsetWidth(), (int) getOffsetHeight(), (int) borderRadius));
        }
    }

}

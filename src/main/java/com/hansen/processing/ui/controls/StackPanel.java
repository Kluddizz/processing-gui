package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.adapters.OrientationAdapter;

import processing.core.PGraphics;
import processing.core.PVector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * A StackPanel stacks children in a specific orientation.
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "StackPanel")
@XmlAccessorType(XmlAccessType.NONE)
public class StackPanel extends Control {

    private Orientation orientation;

    public StackPanel() {
        super();
        setOrientation(Orientation.VERTICAL);
    }

    @Override
    public void drawChildren(PGraphics g) {
        PVector childPosition = new PVector();
        for (Control child : children) {
            child.setPosition(childPosition.copy());
            child.drawControl(g);

            switch (orientation) {
                case HORIZONTAL:
                    childPosition.x += child.getBoundingWidth();
                    break;
                case VERTICAL:
                    childPosition.y += child.getBoundingHeight();
                    break;
            }
        }
    }

    @Override
    public void addChild(Control child) {
        super.addChild(child);
        recalculateSize();

        child.addResizeListener((width, height) -> {
            if (!isStretchHorizontal() && !isFit()) {
                setResizeListenersLocked(true);
                recalculateSize();
                setResizeListenersLocked(false);
            }
        });
    }

    /**
     * @return the orientation
     */
    @XmlAttribute(name = "Orientation")
    @XmlJavaTypeAdapter(OrientationAdapter.class)
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Sets the orientation
     * @param orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        recalculateSize();
    }

    /**
     * Recalculates the size of the stack panel by looking at each child size depending on the orientation
     */
    private void recalculateSize() {
        float width = 0.0f;
        float height = 0.0f;

        switch (orientation) {
            case HORIZONTAL:
                for (Control child : children) {
                    width += child.getOffsetWidth();

                    if (child.getBoundingHeight() > height) {
                        height = child.getOffsetHeight();
                    }
                }
                break;
            case VERTICAL:
                for (Control child : children) {
                    height += child.getOffsetHeight();

                    if (child.getOffsetWidth() > width) {
                        width = child.getOffsetWidth();
                    }
                }
                break;
        }

        if (isStretchHorizontal()) {
            width = getOffsetWidth();
        }

        setSize(width, height);
    }
}

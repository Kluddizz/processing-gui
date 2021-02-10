package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.utils.ColorUtils;

import processing.core.PGraphics;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Displays text on the screen with word wrap
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "TextBlock")
@XmlAccessorType(XmlAccessType.NONE)
public class TextBlock extends TextControl {

    @Override
    protected void draw(PGraphics g) {
        if (getText() != null) {
            g.fill(ColorUtils.colorFromString(getFontColor()));
            g.textSize(getFontSize());
            g.textAlign(LEFT, TOP);
            g.text(getText(), 0, 0, width, height);
        }
    }
}

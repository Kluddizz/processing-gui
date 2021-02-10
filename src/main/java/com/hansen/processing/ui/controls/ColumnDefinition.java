package com.hansen.processing.ui.controls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines properties of a column inside a grid.
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "ColumnDefinition")
@XmlAccessorType(XmlAccessType.NONE)
public class ColumnDefinition extends PatternDefinition {

    public ColumnDefinition() {
        super("1*");
    }

    public ColumnDefinition(String pattern) {
        super(pattern);
        setWidth(pattern);
    }

    /**
     * Returns the width pattern value of the definition. It's not the current width in pixels.
     * @return
     */
    @XmlAttribute(name = "Width")
    public String getWidth() {
        return getPattern();
    }

    /**
     * Set the width pattern value. A value without an unit represents a width in pixel (absolute).
     * A value with unit * defines a proportion of the free space available.
     * 
     * <p>
     * 
     * <b>Example:</b>
     * 
     * <p>
     * 
     * <pre>{@code
     * <Grid Width="400">
     *   <ColumnDefinitions>
     *     <!-- This column will be 200 pixels wide -->
     *     <ColumnDefinition Width="200" />
     *     
     *     <!-- This column will be 50 pixels wide -->
     *     <ColumnDefinition Width="1*" />
     *     
     *     <!-- This column will be 150 pixels wide -->
     *     <ColumnDefinition Width="3*" />
     *   </ColumnDefinitions>
     *</Grid>
     * }</pre>
     * 
     * The first column is 200 pixels wide, so there is a free space of 200 pixels left.
     * This free space has 4 (1 + 3) parts, because the second column reserves 1 part and the
     * third 3 parts of the left space. Now the second column width is calculated with
     * this information. One part is 200 / 4 = 50 pixels wide. So the second column takes
     * 1 * 50 = 50 pixels and the third column takes 3 * 50 = 150 pixels.
     * @param width
     */
    public void setWidth(String width) {
        setPattern(width);
    }
}

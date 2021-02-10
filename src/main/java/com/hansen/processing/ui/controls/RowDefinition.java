package com.hansen.processing.ui.controls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Defines properties of a row inside a grid.
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "RowDefinition")
@XmlAccessorType(XmlAccessType.NONE)
public class RowDefinition extends PatternDefinition {

    public RowDefinition() {
        super("1*");
    }

    public RowDefinition(String pattern) {
        super(pattern);
        setHeight(pattern);
    }
    
    /**
     * Returns the height pattern value of the definition. It's not the current height in pixels.
     * @return
     */
    @XmlAttribute(name = "Height")
    public String getHeight() {
        return getPattern();
    }

    /**
     * Set the row pattern value. A value without an unit represents a height in pixel (absolute).
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
     *   <RowDefinitions>
     *     <!-- This column will be 200 pixels wide -->
     *     <RowDefinition Width="200" />
     *     
     *     <!-- This column will be 50 pixels wide -->
     *     <RowDefinition Width="1*" />
     *     
     *     <!-- This column will be 150 pixels wide -->
     *     <RowDefinition Width="3*" />
     *   </RowDefinitions>
     *</Grid>
     * }</pre>
     * 
     * The first row is 200 pixels wide, so there is a free space of 200 pixels left.
     * This free space has 4 (1 + 3) parts, because the second row reserves 1 part and the
     * third 3 parts of the left space. Now the second row height is calculated with
     * this information. One part is 200 / 4 = 50 pixels. So the second row takes
     * 1 * 50 = 50 pixels and the third row takes 3 * 50 = 150 pixels.
     * @param height
     */
    public void setHeight(String height) {
        setPattern(height);
    }
}

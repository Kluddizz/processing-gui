package com.hansen.processing.ui.controls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Base class for grid definitions. Can be used to handle units like *, Auto and absolute values.
 * @author Florian Hansen
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class PatternDefinition {

    private String pattern;
    private float patternValue;

    public PatternDefinition() {
        pattern = "";
    }

    public PatternDefinition(String pattern) {
        this();
        setPattern(pattern);
    }

    /**
     * Sets the pattern, which takes information about units and values
     * @param pattern
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;

        if (pattern.contains("*")) {
            int end = pattern.indexOf("*");
            patternValue = Float.parseFloat(pattern.substring(0, end));
        }
        else if (pattern.equalsIgnoreCase("Auto")) {
            patternValue = 1.0f;
        }
        else {
            patternValue = Float.parseFloat(pattern);
        }
    }

    /**
     * @return the pattern unit
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @return the pattern value
     */
    public float getPatternValue() {
        return patternValue;
    }

}

package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.adapters.AlignmentAdapter;
import com.hansen.processing.ui.adapters.BooleanAdapter;
import com.hansen.processing.ui.adapters.TextAdapter;
import com.hansen.processing.ui.listener.TextChangedListener;
import com.hansen.processing.ui.utils.ColorUtils;

import processing.core.PGraphics;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for text based elements
 * @author Florian Hansen
 *
 */
public abstract class TextControl extends Control {

    private Alignment textAlignment;
    private float fontSize;
    private String text;
    private String fontColor;
    private boolean fitText;
    private List<TextChangedListener> textChangedListeners = new ArrayList<>();

    public TextControl() {
        super();

        setText("");
        setFontSize(12);
        setFontColor("#ff000000");
        setTextAlignment(Alignment.LEFT);
    }

    @Override
    protected void draw(PGraphics g) {
        if (getText() != null) {
            g.fill(ColorUtils.colorFromString(getFontColor()));
            g.textSize(fontSize);
            
            int textWidth = (int) g.textWidth(getText());

            switch (textAlignment) {
                case RIGHT:
                    g.textAlign(LEFT, CENTER);
                    g.text(getText(), getWidth() - textWidth, getHeight() / 2 - 1);
                    break;
                case CENTER:
                    g.textAlign(LEFT, CENTER);
                    g.text(getText(), getWidth() / 2 - textWidth / 2, getHeight() / 2 - 1);
                    break;
               default:
               case LEFT:
                   g.textAlign(LEFT, CENTER);
                   g.text(getText(), 0.0f, getHeight() / 2 - 1);
                   break;
            }
        }
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        update();
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        update();
    }

    @Override
    public void setStretchHorizontal(Boolean stretchHorizontal) {
        super.setStretchHorizontal(stretchHorizontal);
        update();
    }

    @Override
    public void setPadding(Thickness padding) {
        super.setPadding(padding);
        update();
    }

    /**
     * @return the text alignment
     */
    @XmlAttribute(name = "TextAlignment")
    @XmlJavaTypeAdapter(AlignmentAdapter.class)
    public Alignment getTextAlignment() {
        return textAlignment;
    }

    /**
     * @return the size of the font
     */
    @XmlAttribute(name = "FontSize")
    public float getFontSize() {
        return fontSize;
    }

    /**
     * @return the text content
     */
    @XmlAttribute(name = "Text")
    @XmlJavaTypeAdapter(TextAdapter.class)
    public String getText() {
        return text;
    }

    /**
     * @return the color of the font
     */
    @XmlAttribute(name = "FontColor")
    public String getFontColor() {
        return fontColor;
    }

    /**
     * @return true, if the element size should shrink to the size of the text
     */
    @XmlAttribute(name = "FitText")
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean isFitText() {
        return fitText;
    }

    /**
     * Sets the text alignment
     * @param textAlignment
     */
    public void setTextAlignment(Alignment textAlignment) {
        this.textAlignment = textAlignment;
    }

    /**
     * Sets the font size
     * @param fontSize
     */
    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
        update();
    }

    /**
     * Sets the text content
     * @param text
     */
    public void setText(String text) {
        String oldText = this.text;

        if (text == null) {
            this.text = "";
        } else {
            this.text = text;
        }

        update();
        invokeTextChangedListeners(oldText, this.text);
        notifyPropertyChanged("text");
    }

    /**
     * Sets the font color
     * @param fontColor
     */
    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * Whether the element size should shrink to text size or not
     * @param fitText
     */
    public void setFitText(Boolean fitText) {
        this.fitText = fitText;
        update();
    }

    /**
     * Adds a text changed listener
     * @param listener
     */
    public void addTextChangedListener(TextChangedListener listener) {
        textChangedListeners.add(listener);
    }

    /**
     * Informs each text change listener when the text has changed
     * @param oldText
     * @param newText
     */
    private void invokeTextChangedListeners(String oldText, String newText) {
        for (TextChangedListener listener : textChangedListeners) {
            listener.onTextChange(this, oldText, newText);
        }
    }

    /**
     * Updates the element
     */
    protected void update() {
        if (isFitText() && getText() != null) {
            app.textSize(fontSize);
            super.setWidth(app.textWidth(getText()));
        }

        if (isFitText() || isStretchHorizontal()) {
            super.setHeight(fontSize + padding.getTop() + padding.getBottom());
        }
    }
}
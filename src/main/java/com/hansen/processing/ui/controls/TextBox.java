package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.adapters.TextBoxTypeAdapter;
import com.hansen.processing.ui.utils.ColorUtils;

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
 * A TextBox is used to offer the user input
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "TextBox")
@XmlAccessorType(XmlAccessType.NONE)
public class TextBox extends GradientTextControl {

	private TextBoxType type = TextBoxType.TEXT;
    private boolean isSelected;
    private boolean showCursor;
    private boolean cursorAnimationRunning;
    private int animationDelay;
    private int cursorOffset;
    private String selectedText = "";
    private int selectedTextEnd;
    private int selectedTextStart;
    
    /**
     * @return the value type of the TextBox
     */
    @XmlAttribute(name = "Type")
    @XmlJavaTypeAdapter(TextBoxTypeAdapter.class)
    public TextBoxType getType() {
    	return type;
    }
    
    /**
     * Sets the value type of the TextBox
     * @param type
     */
    public void setType(TextBoxType type) {
    	this.type = type;
    }

    @Override
    protected void setup() {
        animationDelay = 500;
    }

    @Override
    protected void draw(PGraphics g) {
        String text = getText();
        float textOffsetWidth = g.textWidth(text.substring(0, text.length() - cursorOffset));
        float textWidth = g.textWidth(getText());
        float selectedTextWidth = g.textWidth(selectedText);
        float offset = textWidth - textOffsetWidth;
        float cursorHeight = 15.0f;

        super.draw(g);

        switch (getTextAlignment()) {
            case LEFT:
                g.translate(width + 3 - offset, height / 2 - cursorHeight / 2);
                break;

            case RIGHT:
                g.translate(width - offset, height / 2 - cursorHeight / 2);
                break;

            case CENTER:
                g.translate(width / 2 + textWidth / 2 - offset, height / 2 - cursorHeight / 2);
                break;
			default:
				break;
        }

        g.fill(0x40000000);

        if (selectedTextStart < selectedTextEnd) {
            g.rect(0, 0, selectedTextWidth, cursorHeight);
        } else {
            g.rect(-selectedTextWidth, 0, selectedTextWidth, cursorHeight);
        }

        if (showCursor) {
            g.noStroke();

            g.fill(ColorUtils.colorFromString(getFontColor()));
            g.rect(0, 0, 2, cursorHeight);
        }
    }
    
    @Override
    protected void drag(PVector dragDirection, boolean precise) {
    	if (type == TextBoxType.NUMBER) {
    		float value = 0.0f;
    		
    		if (!getText().isEmpty()) {
        		value = Float.parseFloat(getText());
    		}
    		
    		if (!precise) {
    			value += dragDirection.x;    			
    		} else {
    			value += dragDirection.x * 0.01f;
    		}
    		
    		setText(String.valueOf(value));
    	}
    }

    @Override
    protected void hoverEnter() {
        app.cursor(TEXT);
    }

    @Override
    protected void hoverLeave() {
        app.cursor(ARROW);
    }

    @Override
    protected void click(MouseEvent event) {
    	// clear selection from previous TextBoxes to avoid multiple selection behavior
    	deselectTextBoxes();
    	
    	// mark this TextBox as selected
        isSelected = true;

        // start cursor animation
        if (!cursorAnimationRunning) {
            startCursorStateAnimation();
        }
    }

    @Override
    protected void clickBeside(MouseEvent event) {
        isSelected = false;

        selectedTextStart = 0;
        selectedTextEnd = 0;
        selectedText = "";
    }

    @Override
    protected void keyPressed(KeyEvent event) {
        if (isSelected) {
            switch (event.getKeyCode()) {
                case BACKSPACE:
                    if (getText().length() > 0) {
                        if (selectedText.length() > 0) {
                            if (selectedTextStart < selectedTextEnd) {
                                setText(getText().substring(0, selectedTextStart) + getText().substring(selectedTextEnd));
                                cursorOffset -= selectedText.length();
                            } else {
                                setText(getText().substring(0, selectedTextEnd) + getText().substring(selectedTextStart));
                            }


                            selectedTextStart = 0;
                            selectedTextEnd = 0;
                            selectedText = "";
                        } else if (event.isShiftDown() || event.isControlDown()) {
                            setText(null);
                        } else if (cursorOffset < getText().length()) {
                            String front = getText().substring(0, getText().length() - cursorOffset - 1);
                            String back = getText().substring(getText().length() - cursorOffset);
                            setText(front + back);
                        }
                    }
                    break;

                case LEFT:
                    if (cursorOffset < getText().length()) {
                        if (event.isShiftDown()) {
                            selectedTextStart--;

                            if (selectedTextStart < selectedTextEnd) {
                                selectedText = getText().substring(selectedTextStart, selectedTextEnd);
                            } else {
                                selectedText = getText().substring(selectedTextEnd, selectedTextStart);
                            }
                        } else {
                            selectedText = "";
                            selectedTextEnd = 0;
                            selectedTextStart = 0;
                        }

                        cursorOffset++;
                    }
                    break;

                case RIGHT:
                    if (cursorOffset > 0) {
                        if (event.isShiftDown()) {
                            selectedTextStart++;

                            if (selectedTextStart < selectedTextEnd) {
                                selectedText = getText().substring(selectedTextStart, selectedTextEnd);
                            } else {
                                selectedText = getText().substring(selectedTextEnd, selectedTextStart);
                            }
                        } else {
                            selectedText = "";
                            selectedTextEnd = 0;
                            selectedTextStart = 0;
                        }

                        cursorOffset--;
                    }
                    break;

                case ENTER:
                    isSelected = false;
                    break;

                case UP:
                case DOWN:
                case ALT:
                case CONTROL:
                    break;

                case SHIFT:
                    if (selectedTextStart == 0 && selectedTextEnd == 0) {
                        selectedTextEnd = getText().length() - cursorOffset;
                        selectedTextStart = selectedTextEnd;
                    }
                    break;

                default:
                    String front = getText().substring(0, getText().length() - cursorOffset);
                    String back = getText().substring(getText().length() - cursorOffset);
                    setText(front + event.getKey() + back);
                    break;
            }
        }
    }

    /**
     * Starts a thread to let the cursor blink
     */
    private void startCursorStateAnimation() {
        new Thread(() -> {
            cursorAnimationRunning = true;

            while(isSelected) {
                showCursor = true;

                try {
                    Thread.sleep(animationDelay);
                } catch(Exception ignore) { }

                showCursor = false;

                try {
                    Thread.sleep(animationDelay);
                } catch(Exception ignore) { }
            }

            cursorAnimationRunning = false;
        }).start();
    }
    
    /**
     * Deselects all TextBoxes to avoid multiple select
     */
    private void deselectTextBoxes() {
    	for (TextBox textBox : Control.getControls(TextBox.class)) {
    		textBox.isSelected = false;
    	}
    }
}

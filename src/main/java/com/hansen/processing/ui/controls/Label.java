package com.hansen.processing.ui.controls;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Displays text on screen
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "Label")
public class Label extends TextControl {

    public Label() {
        super();
    }

    public Label(String text) {
        super();

        setText(text);
        setFitText(true);
    }

}

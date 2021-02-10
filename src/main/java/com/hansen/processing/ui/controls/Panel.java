package com.hansen.processing.ui.controls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A Container for control elements
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "Panel")
@XmlAccessorType(XmlAccessType.NONE)
public class Panel extends Control {

    public Panel() {
        super();
    }

}

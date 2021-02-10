package com.hansen.processing.ui.controls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.hansen.processing.ui.utils.ColorUtils;

import processing.core.PGraphics;
import processing.event.MouseEvent;

/**
 * Represents a boolean state.
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "CheckBox")
@XmlAccessorType(XmlAccessType.NONE)
public class CheckBox extends Control {

	private boolean checked;
	private String checkColor;
	
	public CheckBox() {
		super();
		
		setSize(12, 12);
		setBackgroundColor("#ffaaaaaa");
		setCheckColor("#ff000000");
	}
	
	@Override
	protected void draw(PGraphics g) {
		if (isChecked()) {
			g.strokeWeight(2);
			g.stroke(ColorUtils.colorFromString(getCheckColor()));
			
			g.line(3, getOffsetHeight() - 6, 6, getOffsetHeight() - 3);
			g.line(6, getOffsetHeight() - 3, getOffsetWidth() - 3, 3);
		}
	}
	
	@Override
	protected void click(MouseEvent event) {
		setChecked(!isChecked());
		notifyPropertyChanged("checked");
	}
	
	/**
	 * Returns true, if the control is checked
	 * @return
	 */
	@XmlAttribute(name = "Checked")
	public boolean isChecked() {
		return checked;
	}
	
	/**
	 * Sets the value of the CheckBox
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	/**
	 * Returns the check sign color string
	 * @return
	 */
	@XmlAttribute(name = "CheckColor")
	public String getCheckColor() {
		return checkColor;
	}
	
	/**
	 * Sets the color of the check sign
	 * @param checkColor
	 */
	public void setCheckColor(String checkColor) {
		this.checkColor = checkColor;
	}
	
}

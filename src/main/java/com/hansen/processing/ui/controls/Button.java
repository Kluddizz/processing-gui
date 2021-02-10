package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.events.ClickEventArgs;
import com.hansen.processing.ui.events.Event;

import processing.event.MouseEvent;

import javax.xml.bind.annotation.*;

/**
 * Control to allow simple click interaction inside an user interface.
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "Button")
@XmlAccessorType(XmlAccessType.NONE)
public class Button extends GradientTextControl {
	
	private Event<ClickEventArgs> clickEvent = new Event<>();

    public Button() {
        super();

        // set default style
        setWidth(100);
        setHeight(22);
        setColorTop("#ff777777");
        setColorBottom("#ff444444");
        setColorTopHover("#ff999999");
        setColorBottomHover("#ff666666");
        setFontColor("#ffeeeeee");
        setTextAlignment(Alignment.CENTER);
    }

    @Override
    protected void click(MouseEvent event) {
    	clickEvent.invoke(this, new ClickEventArgs());
    }

    @Override
    protected void hoverEnter() {
        app.cursor(HAND);
    }

    @Override
    protected void hoverLeave() {
        app.cursor(ARROW);
    }
    
    /**
     * @return The click event, which can be used to register listeners on.
     */
    public Event<ClickEventArgs> getClickEvent() {
		return clickEvent;
	}
}

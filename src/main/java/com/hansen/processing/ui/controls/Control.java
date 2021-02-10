package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.adapters.*;
import com.hansen.processing.ui.bind.Binding;
import com.hansen.processing.ui.events.NotifyPropertyChanged;
import com.hansen.processing.ui.events.PropertyChangedObserver;
import com.hansen.processing.ui.listener.ClickListener;
import com.hansen.processing.ui.listener.ResizeListener;
import com.hansen.processing.ui.utils.ColorUtils;
import com.hansen.processing.ui.controller.Controller;
import com.hansen.processing.ui.singleton.Context;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Base class for all control elements of an user interface
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "Control")
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Control extends PropertyChangedObserver implements ResizeListener, PConstants {

    private static HashMap<String, Control> controlIds = new HashMap<>();
    private boolean resizeListenersLocked;
    private Map<QName, String> extensions;
    
    protected PApplet app;
    protected String id;
    protected Boolean visible;
    protected boolean stretchHorizontal;
    protected Alignment verticalAlignment;
    protected Alignment horizontalAlignment;
    protected List<Control> children;
    protected Control parent;
    protected PVector position;
    protected PVector offsetPosition;
    protected float width;
    protected float height;
    protected Thickness margin;
    protected Thickness padding;
    protected String backgroundColor;
    protected Controller controller;
    private boolean isFitting;
    protected List<ResizeListener> resizeListeners = new ArrayList<>();
    protected List<ClickListener> clickListeners = new ArrayList<>();
    protected boolean isMouseOver;
    protected boolean isClicked;
    protected PVector dragStart = new PVector();
    protected boolean isMouseOverTriggered;

    public Control(PApplet app) {
        this.app = app;

        String randomId = UUID.randomUUID().toString();
        setId(randomId);
        controlIds.put(randomId, this);

        children = new ArrayList<>();
        position = new PVector();
        offsetPosition = new PVector();
        margin = new Thickness(0);
        padding = new Thickness(0);
        isFitting = false;

        isMouseOver = false;
        isMouseOverTriggered = false;
        verticalAlignment = Alignment.TOP;
        horizontalAlignment = Alignment.LEFT;
        setVisible(true);
        setup();
    }

    public Control() {
        this(Context.getInstance().getApp());
    }
    
    /**
     * Returns all controls of the given type.
     * <p>
     * <b>Example:</b>
     * <p>
     * <pre>{@code
     * List<Button> buttons = getControls(Button.class);
     * }</pre>
     * @param <T> The type of the wanted controls
     * @param controlClass
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T extends Control> List<T> getControls(Class<T> controlClass) {
    	List<T> list = new ArrayList<>();
    	
    	for (Control control : controlIds.values()) {
    		if (control.getClass().isAssignableFrom(controlClass)) {
    			list.add((T) control);
    		}
    	}
    	
    	return list;
    }

    /**
     * Returns a control by its name / id.
     * @param <T>
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
	public static <T extends Control> T getControlById(String id) {
        return (T) controlIds.get(id);
    }

    /**
     * Returns the value of an extension. An extension is a non-property of this class and is added to the extensions map after unmarshalling.
     * @param extensionName
     * @return
     */
    public String getExtensionValue(String extensionName) {
        String result = null;

        if (extensions != null) {
            for (Map.Entry<QName, String> extension : extensions.entrySet()) {

                if (extension.getKey().toString().equalsIgnoreCase(extensionName)) {
                    result = extension.getValue();
                    break;
                }

            }
        }

        return result;
    }
    
    /**
     * Sets the value of an extension. If there is no extension with the given name, it will be created. An extension is a non-property of this class and is added to the extensions map after unmarshalling.
     * @param extensionName
     * @param value
     */
    public void setExtensionValue(String extensionName, String value) {
    	if (extensions == null) {
    		extensions = new HashMap<QName, String>();
    	}
    	
    	extensions.put(new QName(extensionName), value);
    }

    /**
     * Initializes the control and assigns a controller to it and all child elements.
     * @param context
     */
    public void initControl(NotifyPropertyChanged context) {
        for (Control child : children) {
            child.initControl(context);
        }

        init();

        String bindString = getExtensionValue("Bind");

        if (context != null && bindString != null) {

            try {
                Binding binding = Binding.fromString(bindString, context, this);
                addBinding(binding);
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onControlResize(float width, float height) {
        float effectiveWidth = width - margin.getLeft() - margin.getRight();
        float effectiveHeight = height - margin.getTop() - margin.getBottom();

        if (isStretchHorizontal()) {
            setWidth(effectiveWidth - padding.getLeft() - padding.getRight());
        } else if (isFit()) {
            setSize(effectiveWidth, effectiveHeight);
        }

        setMargin(getMargin());
    }

    /**
     * @return true, if resize listeners should not be informed by resize events
     */
    public boolean isResizeListenersLocked() {
        return resizeListenersLocked;
    }

    /**
     * Lock or unlock the resize event.
     * @param resizeListenersLocked
     */
    public void setResizeListenersLocked(boolean resizeListenersLocked) {
        this.resizeListenersLocked = resizeListenersLocked;
    }

    /**
     * True, if element is horizontally stretched
     * @return
     */
    @XmlAttribute(name = "StretchHorizontal")
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean isStretchHorizontal() {
        return stretchHorizontal;
    }

    /**
     * True, if element is visible
     * @return
     */
    @XmlAttribute(name = "Visible")
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean isVisible() {
        return visible;
    }

    /**
     * @return Vertical alignment
     */
    @XmlAttribute(name = "VerticalAlignment")
    @XmlJavaTypeAdapter(AlignmentAdapter.class)
    public Alignment getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * @return Horizontal alignment
     */
    @XmlAttribute(name = "HorizontalAlignment")
    @XmlJavaTypeAdapter(AlignmentAdapter.class)
    public Alignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * 
     * @return Assigned controller
     */
    @XmlAttribute(name = "Controller")
    @XmlJavaTypeAdapter(ControllerAdapter.class)
    public Controller getController() {
        return controller;
    }

    /**
     * @return The name / id
     */
    @XmlAttribute(name = "Name")
    public String getId() {
        return id;
    }

    /**
     * @return The current app
     */
    public PApplet getApp() {
        return app;
    }

    /**
     * @return List of children
     */
    @XmlElementRef
    public List<Control> getChildren() {
        return children;
    }

    /**
     * @return Extension map
     */
    @XmlAnyAttribute
    public Map<QName, String> getExtensions() {
        return extensions;
    }

    /**
     * @return The parent element
     */
    public Control getParent() {
        return parent;
    }

    /**
     * @return The position
     */
    public PVector getPosition() {
        return position;
    }

    /**
     * The offset position is used by control elements like the Grid to offset the position inside an area.
     * @return The offset position.
     */
    public PVector getOffsetPosition() {
        return offsetPosition;
    }

    /**
     * @return The elements width (without margin and padding)
     */
    @XmlAttribute(name = "Width")
    public float getWidth() {
        return width;
    }

    /**
     * @return The elements height (without margin and padding)
     */
    @XmlAttribute(name = "Height")
    public float getHeight() {
        return height;
    }

    /**
     * @return The margin (outer space)
     */
    @XmlAttribute(name = "Margin")
    @XmlJavaTypeAdapter(ThicknessAdapter.class)
    public Thickness getMargin() {
        return margin;
    }

    /**
     * @return The padding (inner space)
     */
    @XmlAttribute(name = "Padding")
    @XmlJavaTypeAdapter(ThicknessAdapter.class)
    public Thickness getPadding() {
        return padding;
    }

    /**
     * @return The background color
     */
    @XmlAttribute(name = "Background")
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @return True, if the width and height fits the parent size
     */
    @XmlAttribute(name = "Fit")
    @XmlJavaTypeAdapter(BooleanAdapter.class)
    public Boolean isFit() {
        return isFitting;
    }

    /**
     * Sets the vertical alignment
     * @param verticalAlignment is LEFT, RIGHT or CENTER
     */
    public void setVerticalAlignment(Alignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        setMargin(getMargin());
    }

    /**
     * Sets the horizontal alignment
     * @param horizontalAlignment is TOP, BOTTOM or CENTER
     */
    public void setHorizontalAlignment(Alignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        setMargin(getMargin());
    }

    /**
     * Sets the name / id
     * @param id is the identifier of the element
     */
    public void setId(String id) {
        controlIds.remove(this.id);
        controlIds.put(id, this);
        this.id = id;
    }

    /**
     * Whether stretch the size horizontally or not
     * @param stretchHorizontal
     */
    public void setStretchHorizontal(Boolean stretchHorizontal) {
        this.stretchHorizontal = stretchHorizontal;

        if (parent != null) {
            setWidth(parent.getWidth());
        }
    }

    /**
     * Whether the element should be rendered or not
     * @param visible
     */
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    /**
     * Sets the extensions
     * @param extensions
     */
    public void setExtensions(Map<QName, String> extensions) {
        this.extensions = extensions;
    }

    /**
     * Sets the children
     * @param children
     */
    public void setChildren(List<Control> children) {
        this.children = children;
    }

    /**
     * Sets the position
     * @param position
     */
    public void setPosition(PVector position) {
        this.position = position;
    }

    /**
     * Sets the offset position. The offset position is used by control elements like the Grid to offset the position inside an area.
     * @param offsetPosition
     */
    public void setOffsetPosition(PVector offsetPosition) {
        this.offsetPosition = offsetPosition;
    }

    /**
     * Sets the parent
     * @param parent
     */
    public void setParent(Control parent) {
    	this.parent = parent;
    	
		if (isStretchHorizontal()) {
			setWidth(parent.getWidth() - margin.getLeft() - margin.getRight());
		}  
    }

    /**
     * Sets the controller
     * @param controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Sets the position
     * @param x
     * @param y
     */
    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    /**
     * Sets the x component of the position
     * @param x
     */
    public void setX(float x) {
        position.x = x;
    }

    /**
     * Sets the y component of the position
     * @param y
     */
    public void setY(float y) {
        position.y = y;
    }

    /**
     * Sets the width
     * @param width
     */
    public void setWidth(float width) {
        this.width = width;

        if (!isResizeListenersLocked()) {
            invokeResizeListeners();
        }
    }

    /**
     * Sets the height
     * @param height
     */
    public void setHeight(float height) {
        this.height = height;

        if (!isResizeListenersLocked()) {
            invokeResizeListeners();
        }
    }

    /**
     * Sets the size
     * @param width
     * @param height
     */
    public void setSize(float width, float height) {
        setWidth(width);
        setHeight(height);
    }

    /**
     * Sets the margin (outer space)
     * @param margin
     */
    public void setMargin(Thickness margin) {
    	// override margins top and bottom value based on the vertical alignment
        switch (verticalAlignment) {
            case TOP:
                break;
            case BOTTOM:
            	if (parent != null) {
                    float dy = (parent.getHeight() - getOffsetHeight());
                    margin.setTop(dy);
                    margin.setBottom(0);
                }
                break;
            case CENTER:
                if (parent != null) {
                    float dy = (parent.getHeight() - getOffsetHeight()) / 2.0f;
                    margin.setTop(dy);
                    margin.setBottom(dy);
                }
                break;
            default:
            	break;
        }

    	// override margins left and right value based on the horizontal alignment
        switch (horizontalAlignment) {
            case LEFT:
                break;
            case RIGHT:
                break;
            case CENTER:
                if (parent != null) {
                    float dx = (parent.getWidth() - getOffsetWidth()) / 2.0f;
                    margin.setLeft(dx);
                    margin.setRight(dx);
                }
                break;
            default:
            	break;
        }

        this.margin = margin;
    }

    /**
     * Sets the padding (inner space)
     * @param padding
     */
    public void setPadding(Thickness padding) {
        this.padding = padding;
    }

    /**
     * Sets the background color (starting with {@code #} followed by hex value)
     * @param backgroundColor
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Whether the element should fit its parent size
     * @param fitting
     */
    public void setFit(Boolean fitting) {
        isFitting = fitting;
    }

    /**
     * Add a child
     * @param child
     */
    public void addChild(Control child) {
        children.add(child);
        resizeListeners.add(child);
        child.setParent(this);
    }

    /**
     * Add a resize listener
     * @param resizeListener
     */
    public void addResizeListener(ResizeListener resizeListener) {
        resizeListeners.add(resizeListener);
    }

    /**
     * Add a click listener
     * @param clickListener
     */
    public void addClickListener(ClickListener clickListener) {
        clickListeners.add(clickListener);
    }
    
    /**
     * Draw the element and all of its children, if visible
     */
    public void drawControl(PGraphics g) {
    	if (visible) {
            synchronize();

            g.pushMatrix();
            g.translate(position.x + margin.getLeft(), position.y + margin.getTop());

            g.noStroke();
            g.fill(ColorUtils.colorFromString(backgroundColor));
            g.rect(0, 0, getOffsetWidth(), getOffsetHeight());
            g.translate(padding.getLeft(), padding.getTop());

            draw(g);
            drawChildren(g);

            g.popMatrix();
        }
    }

    /**
     * @return True, if parent is visible
     */
    private boolean isParentVisible() {
        boolean result = true;
        Control currentParent = getParent();

        while (currentParent != null && result) {
            result = currentParent.isVisible();
            currentParent = currentParent.getParent();
        }

        return result;
    }

    /**
     * @return The width including margin
     */
    public float getBoundingWidth() {
        return getOffsetWidth() + margin.getLeft() + margin.getRight();
    }

    /**
     * @return The height including margin
     */
    public float getBoundingHeight() {
        return getOffsetHeight() + margin.getTop() + margin.getBottom();
    }

    /**
     * @return The width including padding
     */
    public float getOffsetWidth() {
        return width + padding.getLeft() + padding.getRight();
    }

    /**
     * @return The height including padding
     */
    public float getOffsetHeight() {
        return height + padding.getTop() + padding.getBottom();
    }

    /**
     * Mouse move event
     * @param event
     */
    public void handleMouseMoved(MouseEvent event) {
        PVector absPos = getAbsolutePosition();
        isMouseOver = event.getX() >= absPos.x && event.getX() <= absPos.x + getOffsetWidth() && event.getY() >= absPos.y && event.getY() <= absPos.y + getOffsetHeight() && isParentVisible() && isVisible();

        for (Control child : children) {
            child.handleMouseMoved(event);
        }

        if (isMouseOver && !isMouseOverTriggered) {
            isMouseOverTriggered = true;
            hoverEnter();
        } else if (!isMouseOver && isMouseOverTriggered) {
            isMouseOverTriggered = false;
            hoverLeave();
        }
    }

    /**
     * Mouse click event
     * @param event
     * @return
     */
    public boolean handleMouseClicked(MouseEvent event) {
        PVector absPos = getAbsolutePosition();
        isMouseOver = event.getX() >= absPos.x && event.getX() <= absPos.x + getOffsetWidth() && event.getY() >= absPos.y && event.getY() <= absPos.y + getOffsetHeight() && isParentVisible() && isVisible();

        boolean childClicked = false;

        for (int i = children.size() - 1; i >= 0 && !childClicked; i--) {
            Control child = children.get(i);
            childClicked = child.handleMouseClicked(event);
        }

        if (isMouseOver && !childClicked) {
            isClicked = true;
            dragStart = new PVector(event.getX(), event.getY());
            click(event);
            invokeClickListeners();
        } else {
        	isClicked = false;
            clickBeside(event);
        }

        return isMouseOver || childClicked;
    }
    
    /**
     * Mouse drag event
     * @param event
     */
    public void handleMouseDrag(MouseEvent event) {
    	if (isClicked) {
    		PVector dragTarget = new PVector(event.getX(), event.getY());
    		PVector movement = PVector.sub(dragTarget, dragStart);
    		dragStart = dragTarget;
    		drag(movement.normalize(), event.isShiftDown());
    	}
    	
    	for (Control child : children) {
    		child.handleMouseDrag(event);
    	}
    }

    /**
     * Key pressed event
     * @param event
     */
    public void handleKeyPressed(KeyEvent event) {
        keyPressed(event);

        for (Control child : children) {
            child.handleKeyPressed(event);
        }
    }

    /**
     * Key released event
     * @param event
     */
    public void handleKeyReleased(KeyEvent event) {
        keyReleased(event);

        for (Control child : children) {
            child.handleKeyReleased(event);
        }
    }
    
    /**
     * Mouse pressed event
     * @param event
     */
    public void handleMousePressed(MouseEvent event) {
    	mousePressed(event);
    	
    	for (Control child : children) {
            child.mousePressed(event);
        }
    }
    
    /**
     * Mouse released event
     * @param event
     */
    public void handleMouseReleased(MouseEvent event) {
    	mouseReleased(event);
    	
    	for (Control child : children) {
            child.handleMouseReleased(event);
        }
    }
    
    /**
     * Mouse wheel event
     * @param event
     */
    public void handleMouseWheel(MouseEvent event) {
    	mouseWheel(event);
    	
    	for (Control child : children) {
            child.handleMouseWheel(event);
        }
    }

    /**
     * @return the absolute position
     */
    public PVector getAbsolutePosition() {
        PVector absolutePosition = new PVector(0, 0);
        Control currentParent = getParent();

        while (currentParent != null) {
            absolutePosition.add(currentParent.position);
            absolutePosition.add(currentParent.offsetPosition);
            absolutePosition.add(new PVector(currentParent.getMargin().getLeft(), currentParent.getMargin().getTop()));
            absolutePosition.add(new PVector(currentParent.getPadding().getLeft(), currentParent.getPadding().getTop()));
            currentParent = currentParent.getParent();
        }

        absolutePosition.add(offsetPosition);
        absolutePosition.add(position);
        absolutePosition.add(new PVector(margin.getLeft(), margin.getTop()));
        return absolutePosition;
    }

    /**
     * Informs all resize listeners
     */
    protected void invokeResizeListeners() {
        for (ResizeListener listener : resizeListeners) {
            listener.onControlResize(getOffsetWidth(), getOffsetHeight());
        }
    }

    /**
     * Informs all click listeners
     */
    protected void invokeClickListeners() {
        for (ClickListener listener : clickListeners) {
            listener.onClick(this);
        }
    }

    /**
     * Initialized the element and its children
     */
    protected void init() {
        List<Control> children = new ArrayList<>(this.children);
        this.children.clear();

        for (Control child : children) {
            addChild(child);
        }
    }
    
    /**
     * Render all children
     */
    protected void drawChildren(PGraphics g) {
        for (Control child : children) {
            child.drawControl(g);
        }
    }
    
    /**
     * Remove all children
     */
    public void clear() {
    	for (int i = children.size() - 1; i >= 0; i--) {
    		Control removed = children.remove(i);
    		controlIds.remove(removed.getId());
    		removed.resizeListeners.clear();
    		
    		removed.clear();
    	}
    }
    
    protected void setup() {

    }

    protected void draw(PGraphics g) {

    }

    protected void click(MouseEvent event) {

    }

    protected void clickBeside(MouseEvent event) {

    }

    protected void hoverEnter() {

    }

    protected void hoverLeave() {

    }

    protected void keyPressed(KeyEvent event) {

    }

    protected void keyReleased(KeyEvent event) {

    }

    protected void mouseWheel(MouseEvent event) {

	}
	
    protected void mousePressed(MouseEvent event) {
		
	}

    protected void mouseReleased(MouseEvent event) {

	}
    
    protected void drag(PVector dragDirection, boolean precise) {
    	
    }

}

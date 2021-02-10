package com.hansen.processing.ui.controls;

import com.hansen.processing.ui.listener.ResizeListener;
import com.hansen.processing.ui.structures.Tuple;

import processing.core.PGraphics;
import processing.core.PVector;

import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Grid separates an area into smaller areas defined by rows and columns.
 * @author Florian Hansen
 *
 */
@XmlRootElement(name = "Grid")
@XmlAccessorType(XmlAccessType.NONE)
public class Grid extends Control {

    private List<RowDefinition> rowDefinitions;
    private List<ColumnDefinition> columnDefinitions;

    private HashMap<RowDefinition, Float> rowHeights;
    private HashMap<RowDefinition, PVector> rowPositions;
    private HashMap<ColumnDefinition, Float> columnWidths;
    private HashMap<ColumnDefinition, PVector> columnPositions;
    private HashMap<RowDefinition, HashMap<ColumnDefinition, Control>> mappedChildren;
    private HashMap<Control, Tuple<RowDefinition, ColumnDefinition>> childPositions;
    private HashMap<Control, Integer> childColumnSpans;
    private HashMap<Control, Integer> childRowSpans;

    public Grid() {
        super();
        rowDefinitions = new ArrayList<>();
        columnDefinitions = new ArrayList<>();

        rowHeights = new HashMap<>();
        rowPositions = new HashMap<>();
        columnWidths = new HashMap<>();
        columnPositions = new HashMap<>();
        mappedChildren = new HashMap<>();
        childPositions = new HashMap<>();
        childColumnSpans = new HashMap<>();
        childRowSpans = new HashMap<>();
    }

    @Override
    public void drawChildren(PGraphics g) {
    	// render each separated area
        for (RowDefinition rowDefinition : rowDefinitions) {
            for (ColumnDefinition columnDefinition : columnDefinitions) {
                float x = columnPositions.get(columnDefinition).x;
                float y = rowPositions.get(rowDefinition).y;

                app.pushMatrix();
                app.translate(x, y);

                if (hasChildAt(rowDefinition, columnDefinition)) {
                    Control child = getChildAt(rowDefinition, columnDefinition);
                    child.drawControl(g);
                }

                app.popMatrix();
            }
        }
    }

    @Override
    protected void init() {
        update();
        setChildren(new ArrayList<>(children));
        update();
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
    public void addChild(Control child) {
        addChild(child, 0, 0);
    }

    /**
     * Add a child and place it into a specific area
     * @param child
     * @param row
     * @param column
     */
    public void addChild(Control child, int row, int column) {
        addChild(child, row, column, 1, 1);
    }

    /**
     * Add a child and place it into a specific area
     * @param child
     * @param row
     * @param column
     * @param rowSpan
     * @param columnSpan
     */
    public void addChild(Control child, int row, int column, int rowSpan, int columnSpan) {
        super.addChild(child);
        addResizeListener(child);

        RowDefinition rowDefinition = rowDefinitions.get(row);
        ColumnDefinition columnDefinition = columnDefinitions.get(column);

        if (!mappedChildren.containsKey(rowDefinition)) {
            mappedChildren.put(rowDefinition, new HashMap<>());
        }

        mappedChildren.get(rowDefinition).put(columnDefinition, child);
        childPositions.put(child, new Tuple<>(rowDefinition, columnDefinition));
        childColumnSpans.put(child, columnSpan);
        childRowSpans.put(child, rowSpan);

        updateChildOffsetPositionAt(rowDefinition, columnDefinition);
        update();
    }

    @Override
    public void setChildren(List<Control> children) {
        this.children.clear();

        for (Control child : children) {
            int row = 0;
            int column = 0;
            int columnSpan = 1;
            int rowSpan = 1;

            if (child.getExtensions() != null) {
                for (Map.Entry<QName, String> entry : child.getExtensions().entrySet()) {
                    if (entry.getKey().toString().equalsIgnoreCase("Row")) {
                        row = Integer.parseInt(entry.getValue());
                    }

                    if (entry.getKey().toString().equalsIgnoreCase("Column")) {
                        column = Integer.parseInt(entry.getValue());
                    }

                    if (entry.getKey().toString().equalsIgnoreCase("ColumnSpan")) {
                        columnSpan = Integer.parseInt(entry.getValue());
                    }

                    if (entry.getKey().toString().equalsIgnoreCase("RowSpan")) {
                        rowSpan = Integer.parseInt(entry.getValue());
                    }
                }
            }

            addChild(child, row, column, rowSpan, columnSpan);
        }
    }

    @Override
    protected void invokeResizeListeners() {
    	// we need to override this method to inform listeners about grid area sizes they relate on
    	
        for (ResizeListener listener : resizeListeners) {
            if (listener instanceof Control && children.contains(listener)) {
                Control child = (Control) listener;
                Tuple<RowDefinition, ColumnDefinition> childPosition = childPositions.get(child);
                int columnSpan = childColumnSpans.get(child);
                int rowSpan = childRowSpans.get(child);

                RowDefinition rowDefinition = childPosition.first;
                ColumnDefinition columnDefinition = childPosition.second;

                // calculate the area width and height
                float width = 0.0f;
                float height = 0.0f;

                for (int i = 0; i < columnSpan; i++) {
                    int next = columnDefinitions.indexOf(columnDefinition) + i;

                    if (next < columnDefinitions.size()) {
                        ColumnDefinition nextColumnDefinition = columnDefinitions.get(next);
                        width += columnWidths.get(nextColumnDefinition);
                    }
                }

                for (int i = 0; i < rowSpan; i++) {
                    int next = rowDefinitions.indexOf(rowDefinition) + i;

                    if (next < rowDefinitions.size()) {
                        RowDefinition nextRowDefinition = rowDefinitions.get(next);
                        height += rowHeights.get(nextRowDefinition);
                    }
                }

                listener.onControlResize(width, height);
            } else {
                listener.onControlResize(getBoundingWidth(), getBoundingHeight());
            }
        }
    }

    /**
     * Get the child at a specific location inside the grid
     * @param rowDefinition
     * @param columnDefinition
     * @return
     */
    public Control getChildAt(RowDefinition rowDefinition, ColumnDefinition columnDefinition) {
        return mappedChildren.get(rowDefinition).get(columnDefinition);
    }

    /**
     * Check whether a cell has a child or not
     * @param rowDefinition
     * @param columnDefinition
     * @return
     */
    public boolean hasChildAt(RowDefinition rowDefinition, ColumnDefinition columnDefinition) {
        return mappedChildren.containsKey(rowDefinition) && mappedChildren.get(rowDefinition).containsKey(columnDefinition);
    }


    /**
     * @return the column definitions
     */
    @XmlElementWrapper(name = "ColumnDefinitions")
    @XmlElementRef
    public List<ColumnDefinition> getColumnDefinitions() {
        return columnDefinitions;
    }

    /**
     * @return the row definitions
     */
    @XmlElementWrapper(name = "RowDefinitions")
    @XmlElementRef
    public List<RowDefinition> getRowDefinitions() {
        return rowDefinitions;
    }

    /**
     * Sets column definitions
     * @param columnDefinitions
     */
    public void setColumnDefinitions(List<ColumnDefinition> columnDefinitions) {
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            addColumnDefinition(columnDefinition);
        }
    }

    /**
     * Sets row definitions
     * @param rowDefinitions
     */
    public void setRowDefinitions(List<RowDefinition> rowDefinitions) {
        for (RowDefinition rowDefinition : rowDefinitions) {
            addRowDefinition(rowDefinition);
        }
    }

    /**
     * Adds a row definition
     * @param rowDefinition
     */
    public void addRowDefinition(RowDefinition rowDefinition) {
        rowDefinitions.add(rowDefinition);
        rowHeights.put(rowDefinition, 0.0f);
        update();
    }

    /**
     * Adds a column definition
     * @param columnDefinition
     */
    public void addColumnDefinition(ColumnDefinition columnDefinition) {
        columnDefinitions.add(columnDefinition);
        columnWidths.put(columnDefinition, 0.0f);
        update();
    }

    /**
     * Updates the cells of this grid
     */
    public void update() {
        calculateRowHeights();
        calculateColumnWidths();

        if (!isFit()) {
            float height = padding.getTop() + padding.getBottom();

            for (float rowHeight : rowHeights.values()) {
                height += rowHeight;
            }

            this.height = height;
        }

        updateRowPositions();
        updateColumnPositions();
        updateChildOffsetPositions();

        invokeResizeListeners();
    }

    /**
     * Calculates every row height
     */
    public void calculateRowHeights() {
        float fills = getFillsVertical();
        float freeSpace = getFreeSpaceVertical();

        rowHeights.clear();

        for (RowDefinition rowDefinition : rowDefinitions) {
            float rowHeight = rowDefinition.getPatternValue();

            if (rowDefinition.getPattern().contains("*")) {
                rowHeight = rowHeight / fills * freeSpace;
            } else if (rowDefinition.getPattern().equalsIgnoreCase("Auto")) {
                rowHeight = getMaxChildHeight(rowDefinition);
            }

            rowHeights.put(rowDefinition, rowHeight);
        }
    }

    /**
     * Calculates every column width
     */
    public void calculateColumnWidths() {
        float fills = getFillsHorizontal();
        float freeSpace = getFreeSpaceHorizontal();

        columnWidths.clear();

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            float columnWidth = columnDefinition.getPatternValue();

            if (columnDefinition.getPattern().contains("*")) {
                columnWidth = columnWidth / fills * freeSpace;
            } else if (columnDefinition.getPattern().equalsIgnoreCase("Auto")) {
                columnWidth = getMaxChildWidth(columnDefinition);
            }

            columnWidths.put(columnDefinition, columnWidth);
        }

    }

    /**
     * Updates the offset positions of every child
     */
    public void updateChildOffsetPositions() {
        for (RowDefinition rowDefinition : rowDefinitions) {
            for (ColumnDefinition columnDefinition : columnDefinitions) {
                updateChildOffsetPositionAt(rowDefinition, columnDefinition);
            }
        }
    }

    /**
     * Updates all row positions
     */
    public void updateRowPositions() {
        rowPositions.clear();

        PVector rowPosition = new PVector(0, 0);

        for (RowDefinition rowDefinition : rowDefinitions) {
            rowPositions.put(rowDefinition, rowPosition.copy());
            rowPosition.y += rowHeights.get(rowDefinition);
        }
    }

    /**
     * Updates all column positions
     */
    public void updateColumnPositions() {
        columnPositions.clear();

        PVector columnPosition = new PVector(0, 0);

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            columnPositions.put(columnDefinition, columnPosition.copy());
            columnPosition.x += columnWidths.get(columnDefinition);
        }
    }

    /**
     * Updates child offset position at a specific cell
     * @param rowDefinition
     * @param columnDefinition
     */
    private void updateChildOffsetPositionAt(RowDefinition rowDefinition, ColumnDefinition columnDefinition) {
        if (hasChildAt(rowDefinition, columnDefinition)) {
            PVector offsetPosition = new PVector();
            offsetPosition.x = columnPositions.get(columnDefinition).x;
            offsetPosition.y = rowPositions.get(rowDefinition).y;

            Control child = getChildAt(rowDefinition, columnDefinition);
            child.setOffsetPosition(offsetPosition);
        }
    }

    /**
     * @param rowDefinition
     * @return the height of the child, which has the highest height
     */
    private float getMaxChildHeight(RowDefinition rowDefinition) {
        float height = 0.0f;

        if (mappedChildren.containsKey(rowDefinition)) {
            for (ColumnDefinition columnDefinition : mappedChildren.get(rowDefinition).keySet()) {
                if (hasChildAt(rowDefinition, columnDefinition)) {
                    Control child = mappedChildren.get(rowDefinition).get(columnDefinition);

                    if (child.getBoundingHeight() > height) {
                        height = child.getBoundingHeight();
                    }
                }
            }
        }

        return height;
    }

    /**
     * @param columnDefinition
     * @return the width of the child, which has the widest width
     */
    private float getMaxChildWidth(ColumnDefinition columnDefinition) {
        float width = 0.0f;

        for (RowDefinition rowDefinition : rowDefinitions) {
            if (hasChildAt(rowDefinition, columnDefinition)) {
                Control child = mappedChildren.get(rowDefinition).get(columnDefinition);

                if (child.getBoundingWidth() > width) {
                    width = child.getBoundingWidth();
                }
            }
        }

        return width;
    }

    /**
     * @return free vertical space
     */
    private float getFreeSpaceVertical() {
        float space = height;

        for (RowDefinition rowDefinition : rowDefinitions) {
            if (!rowDefinition.getPattern().contains("*") && rowHeights.containsKey(rowDefinition)) {
                space -= rowHeights.get(rowDefinition);
            }
        }

        return space;
    }

    /**
     * @return free horizontal space
     */
    private float getFreeSpaceHorizontal() {
        float space = width;

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            if (!columnDefinition.getPattern().contains("*") && columnWidths.containsKey(columnDefinition)) {
                space -= columnWidths.get(columnDefinition);
            }
        }

        return space;
    }

    /**
     * @return number of parts, which divide the vertical free space
     */
    private float getFillsVertical() {
        float n = 0.0f;

        for (RowDefinition rowDefinition : rowDefinitions) {
            if (rowDefinition.getPattern().contains("*")) {
                n += rowDefinition.getPatternValue();
            }
        }

        return n;
    }

    /**
     * @return number of parts, which divide the horizontal free space
     */
    private float getFillsHorizontal() {
        float n = 0.0f;

        for (ColumnDefinition columnDefinition : columnDefinitions) {
            if (columnDefinition.getPattern().contains("*")) {
                n += columnDefinition.getPatternValue();
            }
        }

        return n;
    }
}

package com.hansen.processing.ui.controls;

/**
 * Contains four values to represent a rectangular thickness
 * @author Florian Hansen
 *
 */
public class Thickness {

    private float left;
    private float top;
    private float right;
    private float bottom;

    public Thickness() {
        this(0);
    }

    public Thickness(float leftTopRightBottom) {
        this(leftTopRightBottom, leftTopRightBottom);
    }

    public Thickness(float leftRight, float topBottom) {
        this(leftRight, topBottom, leftRight, topBottom);
    }

    public Thickness(float left, float top, float right, float bottom) {
        setLeft(left);
        setTop(top);
        setRight(right);
        setBottom(bottom);
    }

    @Override
    public String toString() {
        return left + ", " + top + ", " + right + ", " + bottom;
    }

    /**
     * @return the top thickness
     */
    public float getTop() {
        return top;
    }

    /**
     * @return the right thickness
     */
    public float getRight() {
        return right;
    }

    /**
     * @return the bottom thickness
     */
    public float getBottom() {
        return bottom;
    }

    /**
     * @return the left thickness
     */
    public float getLeft() {
        return left;
    }

    /**
     * Sets the top thickness
     * @param top
     */
    public void setTop(float top) {
        this.top = top;
    }

    /**
     * Sets the right thickness
     * @param right
     */
    public void setRight(float right) {
        this.right = right;
    }

    /**
     * Sets the bottom thickness
     * @param bottom
     */
    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    /**
     * Sets the left thickness
     * @param left
     */
    public void setLeft(float left) {
        this.left = left;
    }
}

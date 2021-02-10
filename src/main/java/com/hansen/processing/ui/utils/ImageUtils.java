package com.hansen.processing.ui.utils;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Static class to offer image utilities
 * @author Florian Hansen
 *
 */
public class ImageUtils implements PConstants {

	/**
	 * Creates a rectangular mask with border radius
	 * @param app
	 * @param width
	 * @param height
	 * @param borderRadius
	 * @return
	 */
    public static PGraphics generateMask(PApplet app, int width, int height, int borderRadius) {
        PGraphics mask = app.createGraphics(width, height);

        mask.beginDraw();
        mask.background(0);
        mask.smooth();
        mask.noStroke();
        mask.fill(255);
        mask.rect(0, 0, mask.width, mask.height, borderRadius);
        mask.endDraw();

        return mask;
    }

    /**
     * Generates linear gradient image
     * Source: https://forum.processing.org/one/topic/rounded-rect-with-gradient-background.html
     * @param app
     * @param colorTop
     * @param colorBottom
     * @param width
     * @param height
     * @return
     */
    public static PImage generateGradient(PApplet app, int colorTop, int colorBottom, int width, int height) {
        int tR = (colorTop >> 16) & 0xFF;
        int tG = (colorTop >> 8) & 0xFF;
        int tB = colorTop & 0xFF;
        int bR = (colorBottom >> 16) & 0xFF;
        int bG = (colorBottom >> 8) & 0xFF;
        int bB = colorBottom & 0xFF;

        PImage bg = app.createImage(width, height, RGB);
        bg.loadPixels();

        for (int i=0; i < bg.pixels.length; i++) {
            int y = i/bg.width;
            float n = y/(float)bg.height;

            bg.pixels[i] = app.color(
                    PApplet.lerp(tR, bR, n),
                    PApplet.lerp(tG, bG, n),
                    PApplet.lerp(tB, bB, n),
                    255);
        }

        bg.updatePixels();
        return bg;
    }

}

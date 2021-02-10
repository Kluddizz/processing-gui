package com.hansen.processing.ui.utils;

/**
 * Static class to offer color utilities
 * @author Florian Hansen
 *
 */
public class ColorUtils {

	/**
	 * Converts a color string to an integer representation of Processing
	 * @param color
	 * @return the color in integer representation. If the argument is <b>null</b>, it will return full transparency
	 */
    public static int colorFromString(String color) {
        long result = 0;

        if (color != null) {
            result = Long.valueOf(color.replace("#", ""), 16);
        } else {
            result = Long.valueOf("#00ffffff".replace("#", ""), 16);
        }

        return (int) result;
    }

}

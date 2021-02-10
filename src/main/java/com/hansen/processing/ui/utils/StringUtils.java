package com.hansen.processing.ui.utils;

public class StringUtils {
	
	/**
	 * Builds words out of a string in camel case.
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * {@code isThisCoding } is going to be {@code Is This Coding}
	 * @param camelCase
	 * @return
	 */
	public static String decamel(String camelCase) {
    	String result = "";
    	
    	for (int i = 0; i < camelCase.length(); i++) {
    		char c = camelCase.charAt(i);
    		
    		if (i == 0) {
    			result += Character.toUpperCase(c);
    		} else if (Character.isUpperCase(c)) {
    			result += " " + c;
    		} else {
    			result += c;
    		}
    	}
    	
    	return result;
    }

}

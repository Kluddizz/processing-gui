package com.hansen.processing.ui.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Static class to offer property utilities
 * @author Florian Hansen
 *
 */
public class PropertyUtils {

	/**
	 * Searches a field in a class. In super classes, also.
	 * @param cls
	 * @param fieldName
	 * @return
	 */
    public static Field getField(Class<?> cls, String fieldName) {
        Class<?> currentClass = cls;
        Field field = null;

        while (currentClass != null && field == null) {
            try {
                String saveFieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                field = currentClass.getDeclaredField(saveFieldName);
            } catch (Exception ignore) { }

            currentClass = currentClass.getSuperclass();
        }

        return field;
    }

    /**
     * Searches a method in a class
     * @param cls
     * @param methodName
     * @param parameters
     * @return
     */
    public static Method getMethod(Class<?> cls, String methodName, Class<?>... parameters) {
        Class<?> currentClass = cls;
        Method method = null;

        while (currentClass != null && method == null) {
            try {
                method = currentClass.getDeclaredMethod(methodName, parameters);
            } catch (Exception ignore) { }

            currentClass = currentClass.getSuperclass();
        }

        return method;
    }

    /**
     * Returns the owner of the property path. A property path is something like {@code "property.subProperty.subSubProperty"}.
     * @param object
     * @param property
     * @return
     * @throws IllegalAccessException
     */
    public static Object getPropertyOwner(Object object, String property) throws IllegalAccessException {
        String[] properties = property.split("\\.");

        Object currentObject = object;
        Field field;

        for (int i = 0; i < properties.length - 1; i++) {
            field = getField(currentObject.getClass(), properties[i]);
            field.setAccessible(true);

            currentObject = field.get(currentObject);
        }

        return currentObject;
    }

    /**
     * Returns the getter method of a property
     * @param object
     * @param property
     * @return
     * @throws IllegalAccessException
     */
    public static Method getGetter(Object object, String property) throws IllegalAccessException {
        String[] properties = property.split("\\.");
        Method result;

        Object currentObject = object;
        Field field = getField(currentObject.getClass(), properties[0]);

        for (int i = 0; i < properties.length - 1; i++) {
            field = getField(currentObject.getClass(), properties[i]);
            field.setAccessible(true);

            currentObject = field.get(currentObject);
        }

        String getterName;

        getterName = getGetterName(properties[properties.length - 1]);
        result = getMethod(currentObject.getClass(), getterName);

        if (result == null) {
            getterName = getGetterName(properties[properties.length - 1]).replace("get", "is");
            result = getMethod(currentObject.getClass(), getterName);
        }

        return result;
    }

    /**
     * Returns the setter of a property
     * @param object
     * @param property
     * @return
     * @throws IllegalAccessException
     */
    public static Method getSetter(Object object, String property) throws IllegalAccessException {
        String[] properties = property.split("\\.");
        Method result;

        Object currentObject = object;

        for (int i = 0; i < properties.length - 1; i++) {
            Field field = getField(currentObject.getClass(), properties[i]);
            currentObject = field.get(currentObject);
        }

        Field field = PropertyUtils.getField(currentObject.getClass(), properties[properties.length - 1]);
        String setterName = getSetterName(properties[properties.length - 1]);

        if (field != null) {
            result = getMethod(currentObject.getClass(), setterName, field.getType());
        } else {
            Method getter = getGetter(object, property);
            Class<?> returnType = getter.getReturnType();
            result = getMethod(currentObject.getClass(), setterName, returnType);
        }

        return result;
    }

    /**
     * Returns the property name of a getter or setter
     * @param method
     * @return
     */
    public static String getPropertyName(Method method) {
        String cleanName = method.getName().replace("get", "").replace("set", "").replace("is", "");
        return cleanName.substring(0, 1).toLowerCase() + cleanName.substring(1);
    }

    /**
     * Returns the setter name based on a property name
     * @param propertyName
     * @return
     */
    public static String getSetterName(String propertyName) {
        return "set" + propertyName.replaceFirst(propertyName.substring(0, 1), propertyName.substring(0, 1).toUpperCase());
    }

    /**
     * Return the getter name base on a property name
     * @param propertyName
     * @return
     */
    public static String getGetterName(String propertyName) {
        return "get" + propertyName.replaceFirst(propertyName.substring(0, 1), propertyName.substring(0, 1).toUpperCase());
    }

}

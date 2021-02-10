package com.hansen.processing.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This class is used to mark fields as controls inside a XML file. Those field names need to be equal to the control names.
 * @author Florian Hansen
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ControlReference {

    String value() default "";

}

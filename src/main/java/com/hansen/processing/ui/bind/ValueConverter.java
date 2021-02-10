package com.hansen.processing.ui.bind;

/**
 * ValueConverters can be used to convert types. Basically this is used by view elements
 * to ensure that the correct types are used to visualize data. In the end it converts
 * between frontend and backend types.
 * 
 * An example is that if you want to display an integer in a textbox, then the textbox
 * only can visualize strings. To solve this type issue, a ValueConverter can be used
 * to convert the integer to a string value. Using an instance of this in property bindings
 * will automate the conversion process.
 * 
 * @author Florian Hansen
 *
 * @param <S> Type of the backend property.
 * @param <T> Type of the frontend property.
 */
public interface ValueConverter<S, T> {

	/**
	 * Converts the backend property type to the frontend property type.
	 * @param value Backend value
	 * @return Frontend value
	 */
    public T convert(S value);

    /**
	 * Converts the frontend property type to the backend property type.
	 * @param value Frontend value
	 * @return Backend value
	 */
    public S convertBack(T value);

}

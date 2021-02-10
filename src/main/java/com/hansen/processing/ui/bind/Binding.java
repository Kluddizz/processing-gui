package com.hansen.processing.ui.bind;

import com.hansen.processing.ui.events.NotifyPropertyChanged;
import com.hansen.processing.ui.events.PropertyChangedEventListener;
import com.hansen.processing.ui.utils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Binding {

    private String senderPropertyName;
    private String receiverPropertyName;

    private NotifyPropertyChanged sender;
    private Method senderGetter;
    private Method senderSetter;

    private PropertyChangedEventListener receiver;
    private Method receiverGetter;
    private Method receiverSetter;

    private ValueConverter<Object, Object> valueConverter;

    public static Binding fromString(String bindingString, NotifyPropertyChanged sender, PropertyChangedEventListener receiver)
            throws IllegalAccessException, ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException {
        Binding binding = new Binding();
        binding.setSender(sender);
        binding.setReceiver(receiver);

        // remove brackets and whitespaces to normalize the string
        String normalized = bindingString.replace("{", "")
                                         .replace("}", "")
                                         .replace(" " , "");

        // split comma separated properties
        String[] assignments = normalized.split(",");

        // look up mechanism to read the property values
        for (String assignment : assignments) {
            String key = assignment.split("=")[0];
            String value = assignment.split("=")[1];

            if (key.equalsIgnoreCase("Path")) {
                // the 'path' property indicates the getter and setter methods from an object, which sends
                // events if one property has changed
                Method getter = PropertyUtils.getGetter(sender, value);
                Method setter = PropertyUtils.getSetter(sender, value);

                // we need to look up the property owner, because we want something like a hierarchy structure
                // to work: subobject.property where 'subobject' is another instance holder inside the sender
                Object propertyOwner = PropertyUtils.getPropertyOwner(sender, value);

                // setup the sender instances of the binding object
                binding.setSenderGetter(getter);
                binding.setSenderSetter(setter);
                binding.setSender((NotifyPropertyChanged) propertyOwner);
                binding.setSenderPropertyName(value);
            } else if (key.equalsIgnoreCase("Property")) {
                // the 'property' property indicates the getter and setter methods from the property observer
                Method setter = PropertyUtils.getSetter(receiver, value);
                Method getter = PropertyUtils.getGetter(receiver, value);

                // setup the receiver instances of the binding object
                binding.setReceiverSetter(setter);
                binding.setReceiverGetter(getter);
                binding.setReceiverPropertyName(PropertyUtils.getPropertyName(getter));
            } else if (key.equalsIgnoreCase("Converter")) {
                // a converter can be used to transform properties into a special format
                // read out the class name (incl. package name) and create a new instance based on it
                @SuppressWarnings("unchecked")
				ValueConverter<Object, Object> valueConverter = (ValueConverter<Object, Object>) Class.forName(value).getDeclaredConstructor().newInstance();

                // setup the converter instance of the binding object
                binding.setValueConverter(valueConverter);
            }

        }

        return binding;
    }

    public void receive() {
        try {
            if (valueConverter != null) {
                // if a value converter is present, then use it to first transform the property value
                Object value = valueConverter.convert(senderGetter.invoke(sender));

                // receive property value
                receiverSetter.invoke(receiver, value);
            } else {
            	try {
            		// otherwise just use the property value
            		Object value = senderGetter.invoke(sender);
            		receiverSetter.invoke(receiver, value);            		
            	} catch (IllegalArgumentException e) {
            		System.out.println("The property types don't match. Please use a ValueConverter.");
            		System.out.println(
            				senderGetter.getDeclaringClass().getName() + "." + senderGetter.getName() + " [" + senderGetter.getReturnType().getSimpleName() + "] --/--> " +
            				receiverSetter.getDeclaringClass().getName() + "." + receiverSetter.getName() + " [" + receiverSetter.getParameterTypes()[0].getSimpleName() + "]");
            		System.exit(0);
            	}
            }
        } catch (IllegalAccessException | InvocationTargetException ignore) { }
    }

    public void send() {
        try {
            if (valueConverter != null) {
                // if a value converter is present, then use it to first transform the property value
                Object value = valueConverter.convertBack(receiverGetter.invoke(receiver));

                // send property value
                senderSetter.invoke(sender, value);
            } else {
                // otherwise just use the property value
                Object value = receiverGetter.invoke(receiver);
                senderSetter.invoke(sender, value);
            }
        } catch (IllegalAccessException | InvocationTargetException ignore) { }
    }

    public String getSenderPropertyName() {
        return senderPropertyName;
    }

    public void setSenderPropertyName(String senderPropertyName) {
        this.senderPropertyName = senderPropertyName;
    }

    public String getReceiverPropertyName() {
        return receiverPropertyName;
    }

    public void setReceiverPropertyName(String receiverPropertyName) {
        this.receiverPropertyName = receiverPropertyName;
    }

    public NotifyPropertyChanged getSender() {
        return sender;
    }

    public void setSender(NotifyPropertyChanged sender) {
        this.sender = sender;
    }

    public Method getSenderGetter() {
        return senderGetter;
    }

    public void setSenderGetter(Method senderGetter) {
        this.senderGetter = senderGetter;
    }

    public Method getSenderSetter() {
        return senderSetter;
    }

    public void setSenderSetter(Method senderSetter) {
        this.senderSetter = senderSetter;
    }

    public PropertyChangedEventListener getReceiver() {
        return receiver;
    }

    public void setReceiver(PropertyChangedEventListener receiver) {
        this.receiver = receiver;
    }

    public Method getReceiverGetter() {
        return receiverGetter;
    }

    public void setReceiverGetter(Method receiverGetter) {
        this.receiverGetter = receiverGetter;
    }

    public Method getReceiverSetter() {
        return receiverSetter;
    }

    public void setReceiverSetter(Method receiverSetter) {
        this.receiverSetter = receiverSetter;
    }

    public ValueConverter<?, ?> getValueConverter() {
        return valueConverter;
    }

    public void setValueConverter(ValueConverter<Object, Object> valueConverter) {
        this.valueConverter = valueConverter;
    }
}

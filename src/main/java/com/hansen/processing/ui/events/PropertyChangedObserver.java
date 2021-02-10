package com.hansen.processing.ui.events;

import com.hansen.processing.ui.bind.Binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class to listen on property changed events. This class contains implementations to handle bindings based on property changed events.
 * @author Florian Hansen
 *
 */
public abstract class PropertyChangedObserver implements PropertyChangedEventListener {

    private boolean isSending;
    private boolean isReceiving;
    private Map<NotifyPropertyChanged, Map<String, Binding>> bindings = new HashMap<>();
    private Map<String, List<Binding>> b = new HashMap<>();

    /**
     * @return true, if this class sends an update request to the bound partner
     */
    public boolean isSending() {
        return isSending;
    }

    /**
     * Sets the sending state
     * @param sending
     */
    public void setSending(boolean sending) {
        this.isSending = sending;
    }

    /**
     * @return if this class is receiving a notification from the bound partner
     */
    public boolean isReceiving() {
        return isReceiving;
    }

    /**
     * Sets the receiving state
     * @param receiving
     */
    public void setReceiving(boolean receiving) {
        isReceiving = receiving;
    }

    @Override
    public void onEvent(Object sender, PropertyChangedEventArgs eventArgs) {
        if (!isSending()) {
            setReceiving(true);

            if (bindings.containsKey(sender) && bindings.get(sender).containsKey(eventArgs.getPropertyName())) {
                Binding binding = bindings.get(sender).get(eventArgs.getPropertyName());
                binding.receive();
            }

            setReceiving(false);
        }
    }

    /**
     * Sends information about a change of a property to all bindings with that property
     * @param propertyName
     */
    protected void notifyPropertyChanged(String propertyName) {
        if (!isReceiving()) {
            setSending(true);

            if (b.containsKey(propertyName)) {
                List<Binding> bindings = b.get(propertyName);

                for (Binding binding : bindings) {
                    binding.send();
                }
            }

            setSending(false);
        }
    }

    /**
     * Adds a binding
     * @param binding
     */
    public void addBinding(Binding binding) {
        if (!bindings.containsKey(binding.getSender())) {
            bindings.put(binding.getSender(), new HashMap<>());
        }

        if (!b.containsKey(binding.getReceiverPropertyName())) {
            b.put(binding.getReceiverPropertyName(), new ArrayList<>());
        }

        bindings.get(binding.getSender()).put(binding.getSenderPropertyName(), binding);
        binding.receive();

        b.get(binding.getReceiverPropertyName()).add(binding);

        binding.getSender().addPropertyChangedListener(this);
    }

    /**
     * Syncs binding with each other
     */
    protected void synchronize() {
        if (!isReceiving() && !isSending()) {

            for (List<Binding> bindings : b.values()) {
                for (Binding binding : bindings) {
                    binding.receive();
                }
            }

        }
    }

}

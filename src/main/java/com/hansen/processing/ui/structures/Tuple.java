package com.hansen.processing.ui.structures;

/**
 * This class stores two values.
 * @author Florian Hansen
 *
 * @param <S>
 * @param <T>
 */
public class Tuple<S, T> {

    public S first;
    public T second;

    public Tuple() {

    }

    public Tuple(S first, T second) {
        this.first = first;
        this.second = second;
    }

}

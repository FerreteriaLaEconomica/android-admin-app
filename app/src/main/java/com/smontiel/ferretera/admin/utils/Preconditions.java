package com.smontiel.ferretera.admin.utils;

/**
 * Created by Salvador Montiel on 27/10/18.
 */
public final class Preconditions {

    public static <T> T checkNotNull(T instance) {
        if (instance == null)
            throw new NullPointerException("Instance can not be null");
        return instance;
    }

    public static <T> T checkNotNull(T instance, String message) {
        if (instance == null) throw new NullPointerException(message);
        return instance;
    }

    private Preconditions() {
        throw new RuntimeException("Can not be instantiated");
    }
}

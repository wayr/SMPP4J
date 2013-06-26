package org.wayr.smpp.exceptions;

/**
 *
 * @author paul
 */
public class BadValueTypeException extends RuntimeException {

    public BadValueTypeException() {
    }

    public BadValueTypeException(String string) {
        super(string);
    }
}

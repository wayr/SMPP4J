package org.wayr.smpp.exceptions;

/**
 *
 * @author paul
 */
public class AlreadyBoundException extends RuntimeException {

    public AlreadyBoundException() {
    }

    public AlreadyBoundException(String string) {
        super(string);
    }
}

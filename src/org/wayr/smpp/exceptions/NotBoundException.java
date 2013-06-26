package org.wayr.smpp.exceptions;

/**
 *
 * @author paul
 */
public class NotBoundException extends Exception {

    public NotBoundException() {
    }

    public NotBoundException(String string) {
        super(string);
    }

    public NotBoundException(Throwable thrwbl) {
        super(thrwbl);
    }

    public NotBoundException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
}

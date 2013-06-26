package org.wayr.smpp.exceptions;

/**
 *
 * @author paul
 */
public class NoEncoderException extends RuntimeException {

    protected final Class type;

    public NoEncoderException(Class type) {
        this.type = type;
    }

    public NoEncoderException(Class type, String msg) {
        super(msg);
        this.type = type;
    }

    public Class getType() {
        return type;
    }
}

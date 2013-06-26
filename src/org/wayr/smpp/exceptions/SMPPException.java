package org.wayr.smpp.exceptions;

/**
 *
 * @author paul
 */
public class SMPPException extends Exception {
    static final long serialVersionUID = -5627443821271689144L;

    public SMPPException() {
        super();
    }

    public SMPPException(String s) {
        super(s);
    }

    public SMPPException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }
}


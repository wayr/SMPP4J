package org.wayr.smpp.exceptions;

public class SMPPProtocolException extends RuntimeException {
    static final long serialVersionUID = -3672088785026604214L;

    public SMPPProtocolException(String msg) {
        super(msg);
    }

    public SMPPProtocolException(String msg, Throwable rootCause) {
        super(msg, rootCause);
    }
}


package org.wayr.smpp.exceptions;

/**
 *
 * @author paul
 */
public class VersionException extends RuntimeException {
    static final long serialVersionUID = -4765670763478801170L;

    public VersionException() {
    }

    public VersionException(String msg) {
        super(msg);
    }

    public VersionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}


package org.wayr.smpp.exceptions;

/**
 *
 * @author paul
 */
public class InvalidDateFormatException extends SMPPException {

    private String dateString = "";

    public InvalidDateFormatException() {
    }

    public InvalidDateFormatException(String msg, String dateString) {
        super(msg);
        this.dateString = dateString;
    }

    public String getDateString() {
        return dateString;
    }
}

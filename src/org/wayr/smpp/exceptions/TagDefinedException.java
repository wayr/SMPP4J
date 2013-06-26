package org.wayr.smpp.exceptions;

/**
 *
 * @author paul
 */
public class TagDefinedException extends RuntimeException {

    private int tagValue = -1;

    public TagDefinedException(int tagValue) {
        this.tagValue = tagValue;
    }

    public TagDefinedException(int tagValue, String msg) {
        super(msg);
        this.tagValue = tagValue;
    }

    public int getTagValue() {
        return tagValue;
    }
}


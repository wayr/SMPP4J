package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class NumberOfMessages extends OptionalParameter.ByteParameter {

    public NumberOfMessages() {
        super(Tag.NUMBER_OF_MESSAGES.code());
    }

    public NumberOfMessages(Short tag, Byte value) {
        super(tag, value);
    }

    public NumberOfMessages(Tag tag, Byte value) {
        super(tag, value);
    }
}
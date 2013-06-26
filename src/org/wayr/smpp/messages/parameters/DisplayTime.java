package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class DisplayTime extends OptionalParameter.ByteParameter {

    public DisplayTime() {
        super(Tag.DISPLAY_TIME.code());
    }

    public DisplayTime(Short tag, Byte value) {
        super(tag, value);
    }

    public DisplayTime(Tag tag, Byte value) {
        super(tag, value);
    }
}
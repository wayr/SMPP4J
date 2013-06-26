package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class MessageState extends OptionalParameter.ByteParameter {

    public MessageState() {
        super(Tag.MESSAGE_STATE.code());
    }

    public MessageState(Short tag, Byte value) {
        super(tag, value);
    }

    public MessageState(Tag tag, Byte value) {
        super(tag, value);
    }
}
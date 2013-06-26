package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class PayloadType extends OptionalParameter.ByteParameter {

    public PayloadType() {
        super(Tag.PAYLOAD_TYPE.code());
    }

    public PayloadType(Short tag, Byte value) {
        super(tag, value);
    }

    public PayloadType(Tag tag, Byte value) {
        super(tag, value);
    }
}
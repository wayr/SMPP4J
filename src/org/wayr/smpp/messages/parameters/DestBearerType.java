package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class DestBearerType extends OptionalParameter.ByteParameter {

    public DestBearerType() {
        super(Tag.DEST_BEARER_TYPE.code());
    }

    public DestBearerType(Short tag, Byte value) {
        super(tag, value);
    }

    public DestBearerType(Tag tag, Byte value) {
        super(tag, value);
    }
}
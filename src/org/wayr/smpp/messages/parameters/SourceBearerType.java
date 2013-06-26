package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SourceBearerType extends OptionalParameter.ByteParameter {

    public SourceBearerType() {
        super(Tag.SOURCE_BEARER_TYPE.code());
    }

    public SourceBearerType(Short tag, Byte value) {
        super(tag, value);
    }

    public SourceBearerType(Tag tag, Byte value) {
        super(tag, value);
    }
}
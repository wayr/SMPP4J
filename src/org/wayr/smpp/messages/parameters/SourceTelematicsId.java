package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SourceTelematicsId extends OptionalParameter.ByteParameter {

    public SourceTelematicsId() {
        super(Tag.SOURCE_TELEMATICS_ID.code());
    }

    public SourceTelematicsId(Short tag, Byte value) {
        super(tag, value);
    }

    public SourceTelematicsId(Tag tag, Byte value) {
        super(tag, value);
    }
}
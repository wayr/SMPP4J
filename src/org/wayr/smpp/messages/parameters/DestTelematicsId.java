package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class DestTelematicsId extends OptionalParameter.ShortParameter {

    public DestTelematicsId() {
        super(Tag.DEST_TELEMATICS_ID.code());
    }

    public DestTelematicsId(Short tag, Short value) {
        super(tag, value);
    }

    public DestTelematicsId(Tag tag, Short value) {
        super(tag, value);
    }
}
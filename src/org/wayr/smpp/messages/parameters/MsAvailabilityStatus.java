package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class MsAvailabilityStatus extends OptionalParameter.ByteParameter {

    public MsAvailabilityStatus() {
        super(Tag.MS_AVAILABILITY_STATUS.code());
    }

    public MsAvailabilityStatus(Short tag, Byte value) {
        super(tag, value);
    }

    public MsAvailabilityStatus(Tag tag, Byte value) {
        super(tag, value);
    }
}
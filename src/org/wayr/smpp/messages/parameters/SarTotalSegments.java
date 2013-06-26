package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SarTotalSegments extends OptionalParameter.ByteParameter {

    public SarTotalSegments() {
        super(Tag.SAR_TOTAL_SEGMENTS.code());
    }

    public SarTotalSegments(Short tag, Byte value) {
        super(tag, value);
    }

    public SarTotalSegments(Tag tag, Byte value) {
        super(tag, value);
    }
}
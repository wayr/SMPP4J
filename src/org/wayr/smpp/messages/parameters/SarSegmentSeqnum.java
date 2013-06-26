package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SarSegmentSeqnum extends OptionalParameter.ByteParameter {

    public SarSegmentSeqnum() {
        super(Tag.SAR_SEGMENT_SEQNUM.code());
    }

    public SarSegmentSeqnum(Short tag, Byte value) {
        super(tag, value);
    }

    public SarSegmentSeqnum(Tag tag, Byte value) {
        super(tag, value);
    }
}
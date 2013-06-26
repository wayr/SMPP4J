package org.wayr.smpp.messages;

import org.wayr.smpp.messages.tlv.Tag;
import java.util.HashMap;
import java.util.Map;
import org.wayr.smpp.utils.BytesSerializable;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class OptionalParameters {


    public static OptionalParameter.ShortParameter newSarMsgRefNum(short value) {
        return new OptionalParameter.ShortParameter(Tag.SAR_MSG_REF_NUM, value);
    }

    public static OptionalParameter.ShortParameter newSarMsgRefNum(int value) {
        return newSarMsgRefNum((byte)value);
    }

    public static OptionalParameter.ByteParameter newSarSegmentSeqnum(byte value) {
        return new OptionalParameter.ByteParameter(Tag.SAR_SEGMENT_SEQNUM, value);
    }

    public static OptionalParameter.ByteParameter newSarSegmentSeqnum(int value) {
        return newSarSegmentSeqnum((byte)value);
    }

    public static OptionalParameter.ByteParameter newSarTotalSegments(byte value) {
        return new OptionalParameter.ByteParameter(Tag.SAR_TOTAL_SEGMENTS, value);
    }

    public static OptionalParameter.ByteParameter newSarTotalSegments(int value) {
        return newSarTotalSegments((byte)value);
    }

    public static OptionalParameter.ShortParameter newUserMessageReference(short value) {
        return new OptionalParameter.ShortParameter(Tag.USER_MESSAGE_REFERENCE, value);
    }

    public static OptionalParameter.ShortParameter newUserMessageReference(int value) {
        return new OptionalParameter.ShortParameter(Tag.USER_MESSAGE_REFERENCE, (short) value);
    }

}

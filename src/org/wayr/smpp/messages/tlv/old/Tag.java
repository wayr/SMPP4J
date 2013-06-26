package org.wayr.smpp.messages.tlv.old;

import org.wayr.smpp.messages.tlv.*;
import org.wayr.smpp.messages.tlv.encoders.StringEncoder;
import org.wayr.smpp.messages.tlv.encoders.NullEncoder;
import org.wayr.smpp.messages.tlv.encoders.OctetEncoder;
import org.wayr.smpp.messages.tlv.encoders.NumberEncoder;
import org.wayr.smpp.messages.tlv.encoders.BitmaskEncoder;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import org.wayr.smpp.GlobalConfiguration;
import org.wayr.smpp.exceptions.NoEncoderException;
import org.wayr.smpp.exceptions.TagDefinedException;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class Tag {

    //--
    protected static final Object lock = new Object();
    protected static Map<Integer, Tag> tagTable = new HashMap<Integer, Tag>();
    //--
    public static final Tag DEST_ADDR_SUBUNIT = Tag.define(0x05, Number.class, 1);
    public static final Tag DEST_NETWORK_TYPE = Tag.define(0x06, Number.class, 1);
    public static final Tag DEST_BEARER_TYPE = Tag.define(0x07, Number.class, 1);
    public static final Tag DEST_TELEMATICS_ID = Tag.define(0x08, Number.class, 2);
    public static final Tag SOURCE_ADDR_SUBUNIT = Tag.define(0x0d, Number.class, 1);
    public static final Tag SOURCE_NETWORK_TYPE = Tag.define(0x0e, Number.class, 1);
    public static final Tag SOURCE_BEARER_TYPE = Tag.define(0x0f, Number.class, 1);
    public static final Tag SOURCE_TELEMATICS_ID = Tag.define(0x10, Number.class, 1);
    public static final Tag QOS_TIME_TO_LIVE = Tag.define(0x17, Number.class, 4);
    public static final Tag PAYLOAD_TYPE = Tag.define(0x19, Number.class, 1);
    public static final Tag ADDITIONAL_STATUS_INFO_TEXT = Tag.define(0x1d, String.class, 1, 256);
    public static final Tag RECEIPTED_MESSAGE_ID = Tag.define(0x1e, String.class, 1, 65);
    public static final Tag MS_MSG_WAIT_FACILITIES = Tag.define(0x30, BitSet.class, 1);
    public static final Tag PRIVACY_INDICATOR = Tag.define(0x201, Number.class, 1);
    public static final Tag SOURCE_SUBADDRESS = Tag.define(0x202, byte[].class, 2, 23);
    public static final Tag DEST_SUBADDRESS = Tag.define(0x203, byte[].class, 2, 23);
    public static final Tag USER_MESSAGE_REFERENCE = Tag.define(0x204, Number.class, 2);
    public static final Tag USER_RESPONSE_CODE = Tag.define(0x205, Number.class, 1);
    public static final Tag SOURCE_PORT = Tag.define(0x20a, Number.class, 2);
    public static final Tag DESTINATION_PORT = Tag.define(0x20b, Number.class, 2);
    public static final Tag SAR_MSG_REF_NUM = Tag.define(0x20c, Number.class, 2);
    public static final Tag LANGUAGE_INDICATOR = Tag.define(0x20d, Number.class, 1);
    public static final Tag SAR_TOTAL_SEGMENTS = Tag.define(0x20e, Number.class, 1);
    public static final Tag SAR_SEGMENT_SEQNUM = Tag.define(0x20f, Number.class, 1);
    public static final Tag SC_INTERFACE_VERSION = Tag.define(0x210, Number.class, 1);
    public static final Tag CALLBACK_NUM_PRES_IND = Tag.define(0x302, BitSet.class, 1);
    public static final Tag CALLBACK_NUM_ATAG = Tag.define(0x303, byte[].class, 0, 65);
    public static final Tag NUMBER_OF_MESSAGES = Tag.define(0x304, Number.class, 1);
    public static final Tag CALLBACK_NUM = Tag.define(0x381, byte[].class, 4, 19);
    public static final Tag DPF_RESULT = Tag.define(0x420, Number.class, 1);
    public static final Tag SET_DPF = Tag.define(0x421, Number.class, 1);
    public static final Tag MS_AVAILABILITY_STATUS = Tag.define(0x422, Number.class, 1);
    public static final Tag NETWORK_ERROR_CODE = Tag.define(0x423, byte[].class, 3);
    public static final Tag MESSAGE_PAYLOAD = Tag.define(0x424, byte[].class, -1);
    public static final Tag DELIVERY_FAILURE_REASON = Tag.define(0x425, Number.class, 1);
    public static final Tag MORE_MESSAGES_TO_SEND = Tag.define(0x426, Number.class, 1);
    public static final Tag MESSAGE_STATE = Tag.define(0x427, Number.class, 1);
    public static final Tag USSD_SERVICE_OP = Tag.define(0x501, byte[].class, 1);
    public static final Tag DISPLAY_TIME = Tag.define(0x1201, Number.class, 1);
    public static final Tag SMS_SIGNAL = Tag.define(0x1203, Number.class, 2);
    public static final Tag MS_VALIDITY = Tag.define(0x1204, Number.class, 1);
    public static final Tag ALERT_ON_MESSAGE_DELIVERY = Tag.define(0x130c, null, 0);
    public static final Tag ITS_REPLY_TYPE = Tag.define(0x1380, Number.class, 1);
    public static final Tag ITS_SESSION_INFO = Tag.define(0x1383, byte[].class, 2);
    //--
    //--
    protected Integer tag;
    //--
    protected int minLength = -1;
    protected int maxLength = -1;
    //--
    protected Class type;
    //--
    protected Encoder encoder;

    public Tag(int tag, Class type, Encoder enc, int minLength, int maxLength) throws TagDefinedException {
        this.tag = new Integer(tag);
        this.type = type;
        this.minLength = minLength;
        this.maxLength = maxLength;

        if (enc == null) {
            this.encoder = getEncoderForType(type, GlobalConfiguration.getInstance().getBufferConverter());
        } else {
            this.encoder = enc;
        }
    }

    public int intValue() {
        return tag.intValue();
    }

    public int getLength() {
        return maxLength < 0 ? minLength : maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public Class getType() {
        return type;
    }

    public Encoder getEncoder() {
        return encoder;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tag) {
            return ((Tag) obj).tag.equals(this.tag);
        } else {
            return false;
        }
    }

    public boolean equals(int tag) {
        return tag == this.tag.intValue();
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }

    @Override
    public String toString() {
        return tag.toString();
    }

    public String toHexString() {
        return Integer.toHexString(tag.intValue());
    }

    public static Encoder getEncoderForType(Class type, IOStreamConverter converter) {
        Encoder encoder = null;

        // If type is null and encoder is null, this is a "no value" tlv type.
        if (type == null) {
            encoder = new NullEncoder();
        } else if (Number.class.isAssignableFrom(type)) {
            encoder = new NumberEncoder(converter);
        } else if (String.class.isAssignableFrom(type)) {
            encoder = new StringEncoder(converter);
        } else if (BitSet.class.isAssignableFrom(type)) {
            encoder = new BitmaskEncoder();
        } else if (byte[].class.isAssignableFrom(type)) {
            encoder = new OctetEncoder();
        } else {
            throw new NoEncoderException(type, "No encoder for class type " + type.getName());
        }
        return encoder;
    }

    public static Tag getTag(int tagValue) {
        synchronized (lock) {
            Tag t = (Tag) tagTable.get(new Integer(tagValue));
            if (t == null) {
                return Tag.define(tagValue, byte[].class, null, -1, -1);
            } else {
                return t;
            }
        }
    }

    public static boolean isTagDefined(int tagValue) {
        synchronized (lock) {
            return tagTable.containsKey(new Integer(tagValue));
        }
    }

    public static Tag define(int tag, Class type, int fixedLength) throws TagDefinedException {
        return define(tag, type, null, fixedLength, fixedLength);
    }

    public static Tag define(int tag, Class type, int minLength, int maxLength) throws TagDefinedException {
        return define(tag, type, null, minLength, maxLength);
    }

    public static Tag define(int tag, Class type, Encoder enc, int fixedLength) throws TagDefinedException {
        return define(tag, type, enc, fixedLength, fixedLength);
    }

    public static Tag define(int tag, Class type, Encoder enc, int minLength, int maxLength) throws TagDefinedException {

        Tag t = new Tag(tag, type, enc, minLength, maxLength);

        synchronized (lock) {
            if (tagTable.containsKey(tag)) {
                throw new TagDefinedException(tag, "Tag 0x" + Integer.toHexString(tag) + " is already defined.");
            }

            tagTable.put(tag, t);
        }

        return t;
    }

    public static Tag undefineTag(Tag tag) {
        if (tag == null) {
            return null;
        }
        synchronized (lock) {
            return (Tag) tagTable.remove(tag.tag);
        }
    }
}

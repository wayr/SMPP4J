package org.wayr.smpp.messages.tlv.encoders;

import org.wayr.smpp.exceptions.BadValueTypeException;
import org.wayr.smpp.messages.tlv.old.Encoder;
import org.wayr.smpp.messages.tlv.old.Tag;

/**
 *
 * @author paul
 */
public class OctetEncoder implements Encoder {

    protected static final String BAD_VALUE_MSG = "Value must be of type byte[]";

    public OctetEncoder() {
    }

    @Override
    public void writeTo(Tag tag, Object value, byte[] b, int offset) {
        try {
            byte[] valBytes = (byte[]) value;
            System.arraycopy(valBytes, 0, b, offset, valBytes.length);
        } catch (ClassCastException x) {
            throw new BadValueTypeException(BAD_VALUE_MSG);
        }
    }

    @Override
    public Object readFrom(Tag tag, byte[] b, int offset, int length) {
        byte[] val = new byte[length];
        System.arraycopy(b, offset, val, 0, length);
        return val;
    }

    @Override
    public int getValueLength(Tag tag, Object value) {
        try {
            byte[] b = (byte[]) value;
            return b.length;
        } catch (ClassCastException x) {
            throw new BadValueTypeException(BAD_VALUE_MSG);
        }
    }
}
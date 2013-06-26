package org.wayr.smpp.messages.tlv.encoders;

import org.wayr.smpp.exceptions.BadValueTypeException;
import org.wayr.smpp.messages.tlv.old.Tag;
import org.wayr.smpp.messages.tlv.old.Encoder;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class NumberEncoder implements Encoder {

    protected IOStreamConverter converter;

    public NumberEncoder() {
    }

    public NumberEncoder(IOStreamConverter converter) {
        this.converter = converter;
    }

    @Override
    public void writeTo(Tag tag, Object value, byte[] buffer, int offset) {

        long longVal;
        long mask;
        Number num;
        int len = 1;
        try {
            num = (Number) value;
        } catch (ClassCastException x) {
            throw new BadValueTypeException("Value must be of type java.lang.Number");
        }

        if (value instanceof Byte) {
            mask = 0xff;
        } else if (value instanceof Short) {
            mask = 0xffff;
            len = 2;
        } else if (value instanceof Integer) {
            mask = 0xffffffff;
            len = 4;
        } else {
            mask = 0xffffffffffffffffL;
            len = 8;
        }

        longVal = num.longValue() & mask;
        this.converter.writeNumber(buffer, offset, longVal, len);
    }

    @Override
    public Object readFrom(Tag tag, byte[] b, int offset, int length) {
        Number val = this.converter.readNumber(b, offset, length);

        switch( length ) {
            case 1:
                return val.byteValue();
            case 2:
                return val.shortValue();
            case 4:
                return val.intValue();
            case 8:
            default:
                return val.longValue();
        }
    }

    @Override
    public int getValueLength(Tag tag, Object value) {
        return tag.getLength();
    }
}

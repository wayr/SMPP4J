package org.wayr.smpp.messages.tlv.encoders;

import org.wayr.smpp.messages.tlv.old.Encoder;
import org.wayr.smpp.messages.tlv.old.Tag;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class StringEncoder  implements Encoder {

    protected IOStreamConverter converter;

    public StringEncoder(IOStreamConverter converter) {
        this.converter = converter;
    }

    @Override
    public void writeTo(Tag tag, Object value, byte[] b, int offset) {
        this.converter.writeCString(b, offset, "" + value);
    }

    @Override
    public Object readFrom(Tag tag, byte[] b, int offset, int length) {
        return this.converter.readString(b, offset, length - 1);
    }

    @Override
    public int getValueLength(Tag tag, Object value) {
        return value.toString().length() + 1;
    }

}

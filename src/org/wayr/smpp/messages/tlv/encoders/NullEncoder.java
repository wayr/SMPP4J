package org.wayr.smpp.messages.tlv.encoders;

import org.wayr.smpp.messages.tlv.old.Encoder;
import org.wayr.smpp.messages.tlv.old.Tag;

/**
 *
 * @author paul
 */
public class NullEncoder implements Encoder{

    public NullEncoder() {
    }

    @Override
    public void writeTo(Tag tag, Object value, byte[] b, int offset) {
    }

    @Override
    public Object readFrom(Tag tag, byte[] b, int offset, int length) {
        return null;
    }

    @Override
    public int getValueLength(Tag tag, Object value) {
        return 0;
    }
}

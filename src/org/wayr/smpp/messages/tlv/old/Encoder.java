package org.wayr.smpp.messages.tlv.old;

import org.wayr.smpp.messages.tlv.*;

/**
 *
 * @author paul
 */
public interface Encoder {

    void writeTo(Tag tag, Object value, byte[] b, int offset);

    Object readFrom(Tag tag, byte[] b, int offset, int length);

    int getValueLength(Tag tag, Object value);

}

package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class DestAddrSubunit extends OptionalParameter.ByteParameter {

    public DestAddrSubunit() {
        super(Tag.DEST_ADDR_SUBUNIT.code());
    }

    public DestAddrSubunit(Short tag, Byte value) {
        super(tag, value);
    }

    public DestAddrSubunit(Tag tag, Byte value) {
        super(tag, value);
    }
}

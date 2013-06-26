package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class DestNetworkType extends OptionalParameter.ByteParameter {

    public DestNetworkType() {
        super(Tag.DEST_NETWORK_TYPE.code());
    }

    public DestNetworkType(Short tag, Byte value) {
        super(tag, value);
    }

    public DestNetworkType(Tag tag, Byte value) {
        super(tag, value);
    }
}

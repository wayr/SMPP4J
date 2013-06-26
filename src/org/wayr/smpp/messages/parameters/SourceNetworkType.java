package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SourceNetworkType extends OptionalParameter.ByteParameter {

    public SourceNetworkType() {
        super(Tag.SOURCE_NETWORK_TYPE.code());
    }

    public SourceNetworkType(Short tag, Byte value) {
        super(tag, value);
    }

    public SourceNetworkType(Tag tag, Byte value) {
        super(tag, value);
    }
}
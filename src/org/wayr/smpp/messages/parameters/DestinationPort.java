package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class DestinationPort extends OptionalParameter.ShortParameter {

    public DestinationPort() {
        super(Tag.DESTINATION_PORT.code());
    }

    public DestinationPort(Short tag, Short value) {
        super(tag, value);
    }

    public DestinationPort(Tag tag, Short value) {
        super(tag, value);
    }
}
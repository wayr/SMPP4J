package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SourcePort extends OptionalParameter.ShortParameter {

    public SourcePort() {
        super(Tag.SOURCE_PORT.code());
    }

    public SourcePort(Short tag, Short value) {
        super(tag, value);
    }

    public SourcePort(Tag tag, Short value) {
        super(tag, value);
    }
}
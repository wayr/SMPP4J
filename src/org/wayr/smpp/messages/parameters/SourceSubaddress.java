package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SourceSubaddress extends OptionalParameter.OctetStringParameter {

    public SourceSubaddress() {
        super(Tag.SOURCE_SUBADDRESS.code());
    }

    public SourceSubaddress(Short tag, String value) {
        super(tag, value);
    }

    public SourceSubaddress(Tag tag, String value) {
        super(tag, value);
    }
}
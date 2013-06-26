package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class DestSubaddress extends OptionalParameter.OctetStringParameter {

    public DestSubaddress() {
        super(Tag.DEST_SUBADDRESS.code());
    }

    public DestSubaddress(Short tag, String value) {
        super(tag, value);
    }

    public DestSubaddress(Tag tag, String value) {
        super(tag, value);
    }
}
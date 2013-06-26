package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SourceAddrSubunit extends OptionalParameter.ByteParameter {

    public SourceAddrSubunit() {
        super(Tag.SOURCE_ADDR_SUBUNIT.code());
    }

    public SourceAddrSubunit(Short tag, Byte value) {
        super(tag, value);
    }

    public SourceAddrSubunit(Tag tag, Byte value) {
        super(tag, value);
    }
}
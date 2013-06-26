package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class VendorSpecificSourceMscAddr extends OptionalParameter.OctetStringParameter {

    public VendorSpecificSourceMscAddr() {
        super(Tag.VENDOR_SPECIFIC_SOURCE_MSC_ADDR.code());
    }

    public VendorSpecificSourceMscAddr(Short tag, String value) {
        super(tag, value);
    }

    public VendorSpecificSourceMscAddr(Tag tag, String value) {
        super(tag, value);
    }
}
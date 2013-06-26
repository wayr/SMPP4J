package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class VendorSpecificDestMscAddr extends OptionalParameter.OctetStringParameter {

    public VendorSpecificDestMscAddr() {
        super(Tag.VENDOR_SPECIFIC_DEST_MSC_ADDR.code());
    }

    public VendorSpecificDestMscAddr(Short tag, String value) {
        super(tag, value);
    }

    public VendorSpecificDestMscAddr(Tag tag, String value) {
        super(tag, value);
    }
}
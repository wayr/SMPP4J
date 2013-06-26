package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class BillingIdentification extends OptionalParameter.OctetStringParameter {

    public BillingIdentification() {
        super(Tag.BILLING_IDENTIFICATION.code());
    }

    public BillingIdentification(Short tag, String value) {
        super(tag, value);
    }

    public BillingIdentification(Tag tag, String value) {
        super(tag, value);
    }
}
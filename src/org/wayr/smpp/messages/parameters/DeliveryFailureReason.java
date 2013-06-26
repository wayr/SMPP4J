package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class DeliveryFailureReason extends OptionalParameter.ByteParameter {

    public DeliveryFailureReason() {
        super(Tag.DELIVERY_FAILURE_REASON.code());
    }

    public DeliveryFailureReason(Short tag, Byte value) {
        super(tag, value);
    }

    public DeliveryFailureReason(Tag tag, Byte value) {
        super(tag, value);
    }
}
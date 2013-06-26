package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class AlertOnMessageDelivery extends OptionalParameter.ByteParameter {

    public AlertOnMessageDelivery() {
        super(Tag.ALERT_ON_MESSAGE_DELIVERY.code());
    }

    public AlertOnMessageDelivery(Short tag, Byte value) {
        super(tag, value);
    }

    public AlertOnMessageDelivery(Tag tag, Byte value) {
        super(tag, value);
    }
}
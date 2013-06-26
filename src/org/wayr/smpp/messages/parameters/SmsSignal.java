package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SmsSignal extends OptionalParameter.ShortParameter {

    public SmsSignal() {
        super(Tag.SMS_SIGNAL.code());
    }

    public SmsSignal(Short tag, Short value) {
        super(tag, value);
    }

    public SmsSignal(Tag tag, Short value) {
        super(tag, value);
    }
}
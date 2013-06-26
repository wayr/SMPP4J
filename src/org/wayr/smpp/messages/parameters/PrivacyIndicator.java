package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class PrivacyIndicator extends OptionalParameter.ByteParameter {

    public PrivacyIndicator() {
        super(Tag.PRIVACY_INDICATOR.code());
    }

    public PrivacyIndicator(Short tag, Byte value) {
        super(tag, value);
    }

    public PrivacyIndicator(Tag tag, Byte value) {
        super(tag, value);
    }
}
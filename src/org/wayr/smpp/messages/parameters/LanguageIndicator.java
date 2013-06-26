package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class LanguageIndicator extends OptionalParameter.ByteParameter {

    public LanguageIndicator() {
        super(Tag.LANGUAGE_INDICATOR.code());
    }

    public LanguageIndicator(Short tag, Byte value) {
        super(tag, value);
    }

    public LanguageIndicator(Tag tag, Byte value) {
        super(tag, value);
    }
}
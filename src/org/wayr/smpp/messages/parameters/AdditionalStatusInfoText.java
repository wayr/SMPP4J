package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class AdditionalStatusInfoText extends OptionalParameter.COctetStringParameter {

    public AdditionalStatusInfoText() {
        super(Tag.ADDITIONAL_STATUS_INFO_TEXT.code());
    }

    public AdditionalStatusInfoText(Short tag, String value) {
        super(tag, value);
    }

    public AdditionalStatusInfoText(Tag tag, String value) {
        super(tag, value);
    }
}
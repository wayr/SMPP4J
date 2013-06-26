package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class CallbackNumAtag extends OptionalParameter.OctetStringParameter {

    public CallbackNumAtag() {
        super(Tag.CALLBACK_NUM_ATAG.code());
    }

    public CallbackNumAtag(Short tag, String value) {
        super(tag, value);
    }

    public CallbackNumAtag(Tag tag, String value) {
        super(tag, value);
    }
}
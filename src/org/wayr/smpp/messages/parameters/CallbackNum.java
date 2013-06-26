package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class CallbackNum extends OptionalParameter.OctetStringParameter {

    public CallbackNum() {
        super(Tag.CALLBACK_NUM.code());
    }

    public CallbackNum(Short tag, String value) {
        super(tag, value);
    }

    public CallbackNum(Tag tag, String value) {
        super(tag, value);
    }
}
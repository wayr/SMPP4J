package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class MessagePayload extends OptionalParameter.OctetStringParameter {

    public MessagePayload() {
        super(Tag.MESSAGE_PAYLOAD.code());
    }

    public MessagePayload(Short tag, String value) {
        super(tag, value);
    }

    public MessagePayload(Tag tag, String value) {
        super(tag, value);
    }
}
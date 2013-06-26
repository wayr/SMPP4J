package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class MoreMessagesToSend extends OptionalParameter.ByteParameter {

    public MoreMessagesToSend() {
        super(Tag.MORE_MESSAGES_TO_SEND.code());
    }

    public MoreMessagesToSend(Short tag, Byte value) {
        super(tag, value);
    }

    public MoreMessagesToSend(Tag tag, Byte value) {
        super(tag, value);
    }
}
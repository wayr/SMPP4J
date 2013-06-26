package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class ReceiptedMessageId extends OptionalParameter.COctetStringParameter {

    public ReceiptedMessageId() {
        super(Tag.RECEIPTED_MESSAGE_ID.code());
    }

    public ReceiptedMessageId(Short tag, String value) {
        super(tag, value);
    }

    public ReceiptedMessageId(Tag tag, String value) {
        super(tag, value);
    }
}
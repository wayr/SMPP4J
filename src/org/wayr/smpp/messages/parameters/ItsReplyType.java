package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class ItsReplyType extends OptionalParameter.ByteParameter {

    public ItsReplyType() {
        super(Tag.ITS_REPLY_TYPE.code());
    }

    public ItsReplyType(Short tag, Byte value) {
        super(tag, value);
    }

    public ItsReplyType(Tag tag, Byte value) {
        super(tag, value);
    }
}
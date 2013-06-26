package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class UserResponseCode extends OptionalParameter.ByteParameter {

    public UserResponseCode() {
        super(Tag.USER_RESPONSE_CODE.code());
    }

    public UserResponseCode(Short tag, Byte value) {
        super(tag, value);
    }

    public UserResponseCode(Tag tag, Byte value) {
        super(tag, value);
    }
}
package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class UserMessageReference extends OptionalParameter.ShortParameter {

    public UserMessageReference() {
        super(Tag.USER_MESSAGE_REFERENCE.code());
    }

    public UserMessageReference(Short tag, Short value) {
        super(tag, value);
    }

    public UserMessageReference(Tag tag, Short value) {
        super(tag, value);
    }
}
package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class ItsSessionInfo extends OptionalParameter.ShortParameter {

    public ItsSessionInfo() {
        super(Tag.ITS_SESSION_INFO.code());
    }

    public ItsSessionInfo(Short tag, Short value) {
        super(tag, value);
    }

    public ItsSessionInfo(Tag tag, Short value) {
        super(tag, value);
    }
}
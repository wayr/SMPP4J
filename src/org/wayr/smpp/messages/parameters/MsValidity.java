package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class MsValidity extends OptionalParameter.ByteParameter {

    public MsValidity() {
        super(Tag.MS_VALIDITY.code());
    }

    public MsValidity(Short tag, Byte value) {
        super(tag, value);
    }

    public MsValidity(Tag tag, Byte value) {
        super(tag, value);
    }
}
package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class CallbackNumPresInd extends OptionalParameter.ByteParameter {

    public CallbackNumPresInd() {
        super(Tag.CALLBACK_NUM_PRES_IND.code());
    }

    public CallbackNumPresInd(Short tag, Byte value) {
        super(tag, value);
    }

    public CallbackNumPresInd(Tag tag, Byte value) {
        super(tag, value);
    }
}
package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class UssdServiceOp extends OptionalParameter.ByteParameter {

    public UssdServiceOp() {
        super(Tag.USSD_SERVICE_OP.code());
    }

    public UssdServiceOp(Short tag, Byte value) {
        super(tag, value);
    }

    public UssdServiceOp(Tag tag, Byte value) {
        super(tag, value);
    }
}
package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SetDpf extends OptionalParameter.ByteParameter {

    public SetDpf() {
        super(Tag.SET_DPF.code());
    }

    public SetDpf(Short tag, Byte value) {
        super(tag, value);
    }

    public SetDpf(Tag tag, Byte value) {
        super(tag, value);
    }
}
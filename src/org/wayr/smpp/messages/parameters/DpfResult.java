package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class DpfResult extends OptionalParameter.ByteParameter {

    public DpfResult() {
        super(Tag.DPF_RESULT.code());
    }

    public DpfResult(Short tag, Byte value) {
        super(tag, value);
    }

    public DpfResult(Tag tag, Byte value) {
        super(tag, value);
    }
}
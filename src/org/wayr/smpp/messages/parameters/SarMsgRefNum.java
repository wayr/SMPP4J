package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class SarMsgRefNum extends OptionalParameter.ShortParameter {

    public SarMsgRefNum() {
        super(Tag.SAR_MSG_REF_NUM.code());
    }

    public SarMsgRefNum(Short tag, Short value) {
        super(tag, value);
    }

    public SarMsgRefNum(Tag tag, Short value) {
        super(tag, value);
    }
}
package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class MsMsgWaitFacilities extends OptionalParameter.ByteParameter {

    public MsMsgWaitFacilities() {
        super(Tag.MS_MSG_WAIT_FACILITIES.code());
    }

    public MsMsgWaitFacilities(Short tag, Byte value) {
        super(tag, value);
    }

    public MsMsgWaitFacilities(Tag tag, Byte value) {
        super(tag, value);
    }
}
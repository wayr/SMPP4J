package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class NetworkErrorCode extends OptionalParameter.OctetStringParameter {

    public NetworkErrorCode() {
        super(Tag.NETWORK_ERROR_CODE.code());
    }

    public NetworkErrorCode(Short tag, String value) {
        super(tag, value);
    }

    public NetworkErrorCode(Tag tag, String value) {
        super(tag, value);
    }
}
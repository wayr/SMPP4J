package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class ReplaceSMResponse extends EmptyResponse {

    public ReplaceSMResponse() {
        super(Packet.REPLACE_SM_RESP);
    }

    public ReplaceSMResponse(ReplaceSMRequest q) {
        super(q);
    }
}

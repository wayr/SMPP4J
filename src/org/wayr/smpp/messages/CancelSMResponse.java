package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class CancelSMResponse extends EmptyResponse {

    public CancelSMResponse() {
        super(Packet.CANCEL_SM_RESP);
    }

    public CancelSMResponse(CancelSMRequest q) {
        super(q);
    }
}

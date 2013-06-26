package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class UnbindResponse extends EmptyResponse {

    public UnbindResponse() {
        super(Packet.UNBIND_RESP);
    }

    public UnbindResponse(UnbindRequest request) {
        super(request);
    }

}

package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class BindReceiverResponse extends BindResponse {

    public BindReceiverResponse() {
        super(Packet.BIND_RECEIVER_RESP);
    }

    public BindReceiverResponse(BindReceiverRequest request) {
        super(request);
    }
}

package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class BindTransceiverResponse extends BindResponse {

    public BindTransceiverResponse() {
        super(Packet.BIND_TRANSCEIVER_RESP);
    }

    public BindTransceiverResponse(BindTransceiverRequest request) {
        super(request);
    }
}

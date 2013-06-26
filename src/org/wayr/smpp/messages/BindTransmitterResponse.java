package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class BindTransmitterResponse extends BindResponse {

    public BindTransmitterResponse() {
        super(Packet.BIND_TRANSMITTER_RESP);
    }

    public BindTransmitterResponse(BindTransmitterRequest request) {
        super(request);
    }
}

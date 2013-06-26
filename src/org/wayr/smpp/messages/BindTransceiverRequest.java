package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

public class BindTransceiverRequest extends BindRequest {

    public BindTransceiverRequest() {
        super(Packet.BIND_TRANSCEIVER);
    }
}

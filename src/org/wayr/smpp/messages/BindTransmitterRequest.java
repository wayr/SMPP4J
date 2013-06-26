package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

public class BindTransmitterRequest extends BindRequest {

    public BindTransmitterRequest() {
        super(Packet.BIND_TRANSMITTER);
    }
}

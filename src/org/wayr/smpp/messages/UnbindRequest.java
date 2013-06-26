package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class UnbindRequest extends EmptyRequest {

    public UnbindRequest() {
        super(Packet.UNBIND);
    }
}

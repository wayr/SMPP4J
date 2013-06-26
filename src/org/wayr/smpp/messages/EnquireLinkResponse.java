package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class EnquireLinkResponse extends EmptyResponse {

    public EnquireLinkResponse() {
        super(Packet.ENQUIRE_LINK_RESP);
    }

    public EnquireLinkResponse(EnquireLinkRequest q) {
        super(q);
    }
}

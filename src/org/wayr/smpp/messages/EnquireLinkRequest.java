package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class EnquireLinkRequest extends EmptyRequest {

    public EnquireLinkRequest() {
        super(Packet.ENQUIRE_LINK);
    }
}

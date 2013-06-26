package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class GenericNackResponse extends EmptyResponse {

    /**
     * Construct a new GenericNackResponse.
     */
    public GenericNackResponse() {
        super(Packet.GENERIC_NACK);
    }

}

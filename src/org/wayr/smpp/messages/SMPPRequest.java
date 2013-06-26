package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public abstract class SMPPRequest extends Packet {

    /**
     * false if this packet has been ack'd, true if it has
     */
    protected boolean isAcknowledged;

    /**
     * Construct a new SMPPRequest with specified id.
     */
    protected SMPPRequest(int id) {
        super(id);
    }

    /**
     * Construct a new SMPPRequest with specified sequence number.
     *
     * @param seqNum The sequence number to use
     */
    protected SMPPRequest(int id, int seqNum) {
        super(id, seqNum);
    }

    @Override
    public final boolean isRequest() {
        return true;
    }

    /**
     * Check has this request been acknowledged or not.
     */
    public final boolean isAcknowledged() {
        return isAcknowledged;
    }

    /**
     * Set this request packet to acknowledged.
     */
    public final void ack() {
        isAcknowledged = true;
    }

}

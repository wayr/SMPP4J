package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public abstract class SMPPResponse extends Packet {

    /**
     * Construct a new SMPPResponse with specified command id.
     */
    protected SMPPResponse(int id) {
        super(id);
    }

    /**
     * Construct a new SMPPResponse with specified sequence number.
     *
     * @param seqNum The sequence number to use
     */
    protected SMPPResponse(int id, int seqNum) {
        super(id, seqNum);
    }

    /**
     * Create a new SMPPResponse packet in response to a BindReceiver.
     *
     * @param q The Request packet the response is to
     */
    public SMPPResponse(SMPPRequest q) {
        // Response value is Command ID with Msb set, sequence no. must match
        super(q.getCommandId() | 0x80000000, q.getSequenceNumber());
    }

    /**
     * Set the status of this command (header field)
     *
     * @param s The value for the status
     */
    public void setCommandStatus(int s) {
        this.commandStatus = s;
    }
}

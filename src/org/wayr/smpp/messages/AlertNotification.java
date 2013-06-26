package org.wayr.smpp.messages;

import java.nio.ByteBuffer;
import org.wayr.smpp.Address;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class AlertNotification extends SMPPRequest {

    /**
     * Create a new alert_notification object.
     */
    public AlertNotification() {
        super(ALERT_NOTIFICATION);
    }

    /**
     * Create a new alert_notification object with sequence number
     * <code>seqNum</code>.
     */
    public AlertNotification(int seqNum) {
        super(ALERT_NOTIFICATION, seqNum);
    }

    @Override
    public int getBodyLength() {
        return ((source != null) ? source.getLength() : 3)
                + ((destination != null) ? destination.getLength() : 3);
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        int len = this.getBodyLength();

        byte[] buffer = new byte[len];

        int offset = 0;
        if (source != null) {
            offset = converter.writeBytes(buffer, offset, source.toBytes(converter));
        } else {
            offset = converter.writeBytes(buffer, offset, new Address().toBytes(converter));
        }

        if (destination != null) {
            converter.writeBytes(buffer, offset, destination.toBytes(converter));
        } else {
            converter.writeBytes(buffer, offset, new Address().toBytes(converter));
        }

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] b, int offset, int length) throws SMPPProtocolException {
        this.source = new Address();
        offset = this.source.fromBytes(converter, b, offset, length);

        this.destination = new Address();
        offset = this.destination.fromBytes(converter, b, offset, length);

        return offset;
    }
}

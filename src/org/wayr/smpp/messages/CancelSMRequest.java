package org.wayr.smpp.messages;

import org.wayr.smpp.Address;
import org.wayr.smpp.GSMConstants;
import org.wayr.smpp.Packet;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class CancelSMRequest extends SMPPRequest {

    public CancelSMRequest() {
        super(Packet.CANCEL_SM);
    }

    @Override
    public int getBodyLength() {
        int len = ((serviceType != null) ? serviceType.length() : 0)
                + ((messageId != null) ? messageId.length() : 0)
                + ((source != null) ? source.getLength() : 3)
                + ((destination != null) ? destination.getLength() : 3);

        // 2 c-strings
        return len + 2;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getBodyLength()];

        int offset = 0;
        offset = converter.writeCString(buffer, offset, serviceType);
        offset = converter.writeCString(buffer, offset, this.getMessageId());

        Address s = source;
        if (source == null) {
            s = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, "");
        }
        offset = converter.writeBytes(buffer, offset, s.toBytes(converter));

        s = destination;
        if (destination == null) {
            s = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, "");
        }
        converter.writeBytes(buffer, offset, s.toBytes(converter));

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        serviceType = converter.readCString(body, offset);
        offset += serviceType.length() + 1;

        messageId = converter.readCString(body, offset);
        offset += messageId.length() + 1;

        source = new Address();
        offset = source.fromBytes(converter, body, offset, -1);

        destination = new Address();
        offset = destination.fromBytes(converter, body, offset, -1);

        return offset;
    }
}

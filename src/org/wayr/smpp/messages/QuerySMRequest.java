package org.wayr.smpp.messages;

import org.wayr.smpp.Address;
import org.wayr.smpp.GSMConstants;
import org.wayr.smpp.Packet;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class QuerySMRequest extends SMPPRequest {

    public QuerySMRequest() {
        super(Packet.QUERY_SM);
    }

    @Override
    public int getBodyLength() {
        int len = ((messageId != null) ? messageId.length() : 0)
                + ((source != null) ? source.getLength() : 3);

        // 1 c-string
        return len + 1;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getBodyLength()];
        int offset = 0;

        offset = converter.writeCString(buffer, offset, this.getMessageId());
        Address s = source;
        if (source == null) {
            s = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, "");
        }

        converter.writeBytes(buffer, offset, s.toBytes(converter), 0);

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        messageId = converter.readCString(body, offset);
        offset += messageId.length() + 1;

        source = new Address();
        offset = source.fromBytes(converter, body, offset, -1);

        return offset;
    }
}

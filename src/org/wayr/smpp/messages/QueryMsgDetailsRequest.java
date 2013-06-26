package org.wayr.smpp.messages;

import org.wayr.smpp.Address;
import org.wayr.smpp.GSMConstants;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class QueryMsgDetailsRequest extends SMPPRequest {

    protected int smLength;

    public QueryMsgDetailsRequest() {
        super(QUERY_MSG_DETAILS);
    }

    public void setSmLength(int len) {
        smLength = len;

        if (smLength < 0) {
            smLength = 0;
        }
        if (smLength > 160) {
            smLength = 160;
        }
    }

    /**
     * Get the number of bytes of the original message being requested.
     */
    public int getSmLength() {
        return smLength;
    }

    @Override
    public int getBodyLength() {
        int len = ((messageId != null) ? messageId.length() : 0)
                + ((source != null) ? source.getLength() : 3);

        // 1 1-byte integer, 1 c-string
        return len + 1 + 1;
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
        offset = converter.writeBytes(buffer, offset, s.toBytes(converter), 0);
        converter.writeByte(buffer, offset++, smLength);

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        messageId = converter.readCString(body, offset);
        offset += messageId.length() + 1;

        source = new Address();
        offset = source.fromBytes(converter, body, offset, -1);

        smLength = (int)converter.readByte(body, offset++);

        return offset;
    }
}

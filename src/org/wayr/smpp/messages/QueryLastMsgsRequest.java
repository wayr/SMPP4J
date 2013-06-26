package org.wayr.smpp.messages;

import org.wayr.smpp.Address;
import org.wayr.smpp.GSMConstants;
import org.wayr.smpp.exceptions.InvalidParameterValueException;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class QueryLastMsgsRequest extends SMPPRequest {

    protected int msgCount;

    public QueryLastMsgsRequest() {
        super(QUERY_LAST_MSGS);
    }

    public void setMsgCount(int n) throws InvalidParameterValueException {
        if (n > 0 && n <= 100) {
            this.msgCount = n;
        } else {
            throw new InvalidParameterValueException("Message count must be between 1 and 100", n);
        }
    }

    public int getMsgCount() {
        return msgCount;
    }

    @Override
    public int getBodyLength() {
        int len = (source != null) ? source.getLength() : 3;

        // 1 1-byte integer
        return len + 1;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getBodyLength()];
        int offset = 0;

        Address s = source;
        if (source == null) {
            s = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, "");
        }
        offset = converter.writeBytes(buffer, offset, s.toBytes(converter), 0);
        converter.writeByte(buffer, offset++, msgCount);

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        source = new Address();
        offset = source.fromBytes(converter, body, offset, -1);

        msgCount = (int)converter.readByte(body, offset++);

        return offset;
    }
}

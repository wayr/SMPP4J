package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;
import org.wayr.smpp.exceptions.InvalidDateFormatException;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.utils.SMPPDate;

/**
 *
 * @author paul
 */
public class QuerySMResponse extends SMPPResponse {

    public QuerySMResponse() {
        super(Packet.QUERY_SM_RESP);
    }

    public QuerySMResponse(QuerySMRequest q) {
        super(q);
    }

    @Override
    public int getBodyLength() {
        int len = ((messageId != null) ? messageId.length() : 0)
                + ((finalDate != null) ? finalDate.toString().length() : 0);

        // 2 1-byte integers, 2 c-strings
        return len + 2 + 2;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getBodyLength()];
        int offset = 0;

        String fdate = (finalDate == null) ? null : finalDate.toString();

        offset = converter.writeCString(buffer, offset, this.getMessageId());
        offset = converter.writeCString(buffer, offset, fdate);
        converter.writeByte(buffer, offset++, messageStatus);
        converter.writeByte(buffer, offset++, errorCode);

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        try {
            messageId = converter.readCString(body, offset);
            offset += messageId.length() + 1;


            String finald = converter.readCString(body, offset);
            offset += finald.length() + 1;
            if (finald.length() > 0) {
                finalDate = SMPPDate.parseSMPPDate(finald);
            }

            messageStatus = (int)converter.readByte(body, offset++);
            errorCode = (int)converter.readByte(body, offset++);

            return offset;
        } catch (InvalidDateFormatException e) {
            throw new SMPPProtocolException("Unrecognized date format", e);
        }
    }
}

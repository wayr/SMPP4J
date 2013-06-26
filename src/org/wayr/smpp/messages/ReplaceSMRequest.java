package org.wayr.smpp.messages;

import org.wayr.smpp.Address;
import org.wayr.smpp.GSMConstants;
import org.wayr.smpp.Packet;
import org.wayr.smpp.exceptions.InvalidDateFormatException;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.utils.SMPPDate;

/**
 *
 * @author paul
 */
public class ReplaceSMRequest extends SMPPRequest {

    public ReplaceSMRequest() {
        super(Packet.REPLACE_SM);
    }

    @Override
    public int getBodyLength() {
        int len = ((messageId != null) ? messageId.length() : 0)
                + ((source != null) ? source.getLength() : 3)
                + ((deliveryTime != null) ? deliveryTime.toString().length() : 0)
                + ((expiryTime != null) ? expiryTime.toString().length() : 0)
                + ((message != null) ? message.length : 0);

        // 3 1-byte integers, 3 c-strings
        return len + 3 + 3;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        int smLength = 0;
        if (message != null) {
            smLength = message.length;
        }

        byte[] buffer = new byte[this.getBodyLength()];
        int offset = 0;
        offset = converter.writeCString(buffer, offset, this.getMessageId());

        Address s = source;
        if (s == null) {
            // Write ton=0(null), npi=0(null), address=\0(nul)
            s = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, "");
        }
        offset = converter.writeBytes(buffer, offset, s.toBytes(converter), 0);

        String dt = (deliveryTime == null) ? null : deliveryTime.toString();
        String et = (expiryTime == null) ? null : expiryTime.toString();

        offset = converter.writeCString(buffer, offset, dt);
        offset = converter.writeCString(buffer, offset, et);
        offset = converter.writeByte(buffer, offset, registered);
        offset = converter.writeByte(buffer, offset, defaultMsg);
        offset = converter.writeByte(buffer, offset, smLength);

        if (message != null) {
            converter.writeBytes(buffer, offset, message);
        }
        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        try {
            int smLength;
            String delivery;
            String valid;

            messageId = converter.readCString(body, offset);
            offset += messageId.length() + 1;

            source = new Address();
            source.fromBytes(converter, body, offset, -1);
            offset += source.getLength();

            delivery = converter.readCString(body, offset);
            offset += delivery.length() + 1;
            if (delivery.length() > 0) {
                deliveryTime = SMPPDate.parseSMPPDate(delivery);
            }

            valid = converter.readCString(body, offset);
            offset += valid.length() + 1;
            if (valid.length() > 0) {
                expiryTime = SMPPDate.parseSMPPDate(valid);
            }

            registered = (int)converter.readByte(body, offset++);
            defaultMsg = (int)converter.readByte(body, offset++);
            smLength = (int)converter.readByte(body, offset++);

            if (smLength > 0) {
                message = new byte[smLength];
                System.arraycopy(body, offset, message, 0, smLength);
            }

            return offset + smLength;
        } catch (InvalidDateFormatException e) {
            throw new SMPPProtocolException("Unrecognized date format", e);
        }
    }
}

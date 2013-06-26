package org.wayr.smpp.messages;

import org.wayr.smpp.Address;
import org.wayr.smpp.GSMConstants;
import org.wayr.smpp.exceptions.InvalidDateFormatException;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.utils.SMPPDate;

/**
 *
 * @author paul
 */
public class SubmitSMRequest extends SMPPRequest {

    public SubmitSMRequest() {
        super(SUBMIT_SM);
    }

    @Override
    public int getBodyLength() {
        int len = ((serviceType != null) ? serviceType.length() : 0)
                + ((source != null) ? source.getLength() : 3)
                + ((destination != null) ? destination.getLength() : 3)
                + ((deliveryTime != null) ? deliveryTime.toString().length() : 0)
                + ((expiryTime != null) ? expiryTime.toString().length() : 0)
                + ((message != null) ? message.length : 0);

        // 8 1-byte integers, 3 c-strings
        return len + 8 + 3;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {

        byte[] buffer = new byte[this.getBodyLength()];
        int offset = 0;

        int smLength = 0;
        if (message != null) {
            smLength = message.length;
        }

        offset = converter.writeCString(buffer, offset, serviceType);
        Address s = source;
        if (source == null) {
            s = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, "");
        }
        offset = converter.writeBytes(buffer, offset, s.toBytes(converter), 0);


        s = destination;
        if (destination == null) {
            s = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, "");
        }
        offset = converter.writeBytes(buffer, offset, s.toBytes(converter), 0);

        String dt = (deliveryTime == null) ? "" : deliveryTime.toString();
        String et = (expiryTime == null) ? "" : expiryTime.toString();

        converter.writeByte(buffer, offset++, esmClass);
        converter.writeByte(buffer, offset++, protocolID);
        converter.writeByte(buffer, offset++, priority);
        offset = converter.writeCString(buffer, offset, dt);
        offset = converter.writeCString(buffer, offset, et);
        converter.writeByte(buffer, offset++, registered);
        converter.writeByte(buffer, offset++, replaceIfPresent);
        converter.writeByte(buffer, offset++, dataCoding);
        converter.writeByte(buffer, offset++, defaultMsg);
        converter.writeByte(buffer, offset++, smLength);
        if (message != null) {
            converter.writeBytes(buffer, offset, message);
        }

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        try {
            serviceType = converter.readCString(body, offset);
            offset += serviceType.length() + 1;

            source = new Address();
            offset = source.fromBytes(converter, body, offset, -1);

            destination = new Address();
            offset = destination.fromBytes(converter, body, offset, -1);

            // ESM class, protocol Id, priorityFlag...
            esmClass = (int)converter.readByte(body, offset++);
            protocolID = (int)converter.readByte(body, offset++);
            priority = (int)converter.readByte(body, offset++);

            String delivery = converter.readCString(body, offset);
            offset += delivery.length() + 1;
            if (delivery.length() > 0) {
                deliveryTime = SMPPDate.parseSMPPDate(delivery);
            }

            String valid = converter.readCString(body, offset);
            offset += valid.length() + 1;
            if (valid.length() > 0) {
                expiryTime = SMPPDate.parseSMPPDate(valid);
            }

            // Registered delivery, replace if present, data coding, default msg
            // and message length
            registered = (int)converter.readByte(body, offset++);
            replaceIfPresent = (int)converter.readByte(body, offset++);
            dataCoding = (int)converter.readByte(body, offset++);
            defaultMsg = (int)converter.readByte(body, offset++);
            int smLength = (int)converter.readByte(body, offset++);

            if (smLength > 0) {
                message = new byte[smLength];
                System.arraycopy(body, offset, message, 0, smLength);
            }

            return offset + smLength;
        } catch (InvalidDateFormatException x) {
            throw new SMPPProtocolException("Unrecognized date format", x);
        }
    }
}

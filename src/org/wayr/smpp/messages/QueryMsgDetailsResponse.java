package org.wayr.smpp.messages;

import org.wayr.smpp.Address;
import org.wayr.smpp.GSMConstants;
import org.wayr.smpp.exceptions.InvalidDateFormatException;
import org.wayr.smpp.exceptions.InvalidParameterValueException;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.utils.SMPPDate;

/**
 *
 * @author paul
 */
public class QueryMsgDetailsResponse extends SMPPResponse {

    private DestinationTable destinationTable = new DestinationTable();

    public QueryMsgDetailsResponse() {
        super(QUERY_MSG_DETAILS_RESP);
    }

    public QueryMsgDetailsResponse(QueryMsgDetailsRequest q) {
        super(q);
    }

    public synchronized int addDestination(Address d) {
        destinationTable.add(d);
        return destinationTable.size();
    }

    public synchronized int addDestination(String d) throws InvalidParameterValueException {
        if (!version.validateDistListName(d)) {
            throw new InvalidParameterValueException("Distribution list is invalid", d);
        }

        destinationTable.add(d);
        return destinationTable.size();
    }

    public synchronized DestinationTable getDestinationTable() {
        return destinationTable;
    }

    @Override
    public synchronized int getBodyLength() {
        int size = ((serviceType != null) ? serviceType.length() : 0)
                + ((source != null) ? source.getLength() : 3)
                + ((deliveryTime != null) ? deliveryTime.toString().length() : 0)
                + ((expiryTime != null) ? expiryTime.toString().length() : 0)
                + ((message != null) ? message.length : 0)
                + ((messageId != null) ? messageId.length() : 0)
                + ((finalDate != null) ? finalDate.toString().length() : 0);

        size += destinationTable.getLength();

        // 8 1-byte integers, 5 c-strings
        return size + 8 + 5;
    }

    @Override
    public synchronized byte[] toBytes(IOStreamConverter converter) {
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



        // Get a clone of the table that can't be changed while writing..
        DestinationTable table = (DestinationTable) this.destinationTable.clone();
        int numDests = table.size();
        offset = converter.writeByte(buffer, offset, numDests);
        offset = converter.writeBytes(buffer, offset, table.toBytes(converter));

        String dt = (deliveryTime == null) ? null : deliveryTime.toString();
        String et = (expiryTime == null) ? null : expiryTime.toString();
        String fd = (finalDate == null) ? null : finalDate.toString();

        offset = converter.writeByte(buffer, offset, protocolID);
        offset = converter.writeByte(buffer, offset, priority);
        offset = converter.writeCString(buffer, offset, dt);
        offset = converter.writeCString(buffer, offset, et);
        offset = converter.writeByte(buffer, offset, registered);
        offset = converter.writeByte(buffer, offset, dataCoding);
        if (message != null) {
            converter.writeBytes(buffer, offset, message);
        }
        offset = converter.writeCString(buffer, offset, this.getMessageId());
        offset = converter.writeCString(buffer, offset, fd);

        offset = converter.writeByte(buffer, offset, messageStatus);
        converter.writeByte(buffer, offset++, errorCode);

        return buffer;
    }

    @Override
    public synchronized int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        try {

            serviceType = converter.readCString(body, offset);
            offset += serviceType.length() + 1;

            source = new Address();
            offset = source.fromBytes(converter, body, offset, -1);

            int numDests = (int)converter.readByte(body, offset++);
            DestinationTable dt = new DestinationTable();
            offset = dt.fromBytes(converter, body, offset, numDests);
            this.destinationTable = dt;

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


            registered = (int)converter.readByte(body, offset++);
            dataCoding = (int)converter.readByte(body, offset++);
            int smLength = (int)converter.readByte(body, offset++);
            if (smLength > 0) {
                message = new byte[smLength];
                System.arraycopy(body, offset, message, 0, smLength);
                offset += smLength;
            }

            messageId = converter.readCString(body, offset);
            offset += messageId.length() + 1;

            String finalD = converter.readCString(body, offset);
            offset += finalD.length() + 1;
            if (valid.length() > 0) {
                finalDate = SMPPDate.parseSMPPDate(valid);
            }

            messageStatus = (int)converter.readByte(body, offset++);
            errorCode = (int)converter.readByte(body, offset++);

            return offset;
        } catch (InvalidDateFormatException x) {
            throw new SMPPProtocolException("Unrecognized date format", x);
        }
    }
}

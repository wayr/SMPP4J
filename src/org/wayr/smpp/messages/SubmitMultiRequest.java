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
public class SubmitMultiRequest extends SMPPRequest {

    protected DestinationTable destinationTable = new DestinationTable();

    public SubmitMultiRequest() {
        super(SUBMIT_MULTI);
    }

    public synchronized DestinationTable getDestinationTable() {
        return destinationTable;
    }

    public synchronized void setDestinationTable(DestinationTable destinationTable) {
        this.destinationTable = destinationTable;
    }

    public synchronized int addDestination(Address d) {
        destinationTable.add(d);
        return destinationTable.size();
    }

    public synchronized int addDestination(String d) throws InvalidParameterValueException {

        if (!version.validateDistListName(d)) {
            throw new InvalidParameterValueException("Distribution list name is invalid", d);
        }
        destinationTable.add(d);
        return destinationTable.size();
    }

    /*
     * public int getTableSize() { return destinationTable.size(); }
     */
    @Override
    public synchronized int getBodyLength() {
        int size = ((serviceType != null) ? serviceType.length() : 0)
                + ((source != null) ? source.getLength() : 3)
                + ((deliveryTime != null) ? deliveryTime.toString().length() : 0)
                + ((expiryTime != null) ? expiryTime.toString().length() : 0)
                + ((message != null) ? message.length : 0);

        size += destinationTable.getLength();

        // 9 1-byte integers, 4 c-strings
        return size + 9 + 3;
    }

    @Override
    public synchronized byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getBodyLength()];
        int offset = 0;

        int smLength = 0;
        if (message != null) {
            smLength = message.length;
        }

        // Get a clone of the table that can't be changed while writing..
        DestinationTable table = (DestinationTable) this.destinationTable.clone();

        offset = converter.writeCString(buffer, offset, this.serviceType);
        Address s = source;
        if (source == null) {
            s = new Address(GSMConstants.GSM_TON_UNKNOWN, GSMConstants.GSM_NPI_UNKNOWN, "");
        }
        offset = converter.writeBytes(buffer, offset, s.toBytes(converter), 0);


        int numDests = table.size();
        offset = converter.writeByte(buffer, offset, numDests);
        offset = converter.writeBytes(buffer, offset, table.toBytes(converter));

        String dt = (deliveryTime == null) ? null : deliveryTime.toString();
        String et = (expiryTime == null) ? null : expiryTime.toString();

        offset = converter.writeByte(buffer, offset, esmClass);
        offset = converter.writeByte(buffer, offset, protocolID);
        offset = converter.writeByte(buffer, offset, priority);
        offset = converter.writeCString(buffer, offset, dt);
        offset = converter.writeCString(buffer, offset, et);
        offset = converter.writeByte(buffer, offset, registered);
        offset = converter.writeByte(buffer, offset, replaceIfPresent);
        offset = converter.writeByte(buffer, offset, dataCoding);
        offset = converter.writeByte(buffer, offset, defaultMsg);
        offset = converter.writeByte(buffer, offset, smLength);
        if (message != null) {
            converter.writeBytes(buffer, offset, message);
        }

        return buffer;
    }

    @Override
    public synchronized int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        try {
            int numDests;
            int smLength;
            String delivery;
            String valid;

            // First the service type
            serviceType = converter.readCString(body, offset);
            offset += serviceType.length() + 1;

            source = new Address();
            offset = source.fromBytes(converter, body, offset, -1);

            // Read in the number of destination structures to follow:
            numDests = (int)converter.readByte(body, offset++);

            // Now read in numDests number of destination structs
            DestinationTable dt = new DestinationTable();
            offset = dt.fromBytes(converter, body, offset, numDests);
            this.destinationTable = dt;

            // ESM class, protocol Id, priorityFlag...
            esmClass = (int)converter.readByte(body, offset++);
            protocolID = (int)converter.readByte(body, offset++);
            priority = (int)converter.readByte(body, offset++);

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

            // Registered delivery, replace if present, data coding, default msg
            // and message length
            registered = (int)converter.readByte(body, offset++);
            replaceIfPresent = (int)converter.readByte(body, offset++);
            dataCoding = (int)converter.readByte(body, offset++);
            defaultMsg = (int)converter.readByte(body, offset++);
            smLength = (int)converter.readByte(body, offset++);

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

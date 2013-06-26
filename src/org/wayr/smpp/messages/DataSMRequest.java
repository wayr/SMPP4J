package org.wayr.smpp.messages;

import org.wayr.smpp.Address;
import org.wayr.smpp.GSMConstants;
import org.wayr.smpp.Packet;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class DataSMRequest extends SMPPRequest {

    public DataSMRequest() {
        super(Packet.DATA_SM);
    }

    @Override
    public int getBodyLength() {
        int len = ((serviceType != null) ? serviceType.length() : 0)
                + ((source != null) ? source.getLength() : 3)
                + ((destination != null) ? destination.getLength() : 3);

        // 3 1-byte integers, 1 c-string
        return len + 4;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getBodyLength()];
        int offset = 0;

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


        offset = converter.writeByte(buffer, offset, esmClass);
        offset = converter.writeByte(buffer, offset, registered);
        converter.writeByte(buffer, offset, dataCoding);

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        serviceType = converter.readCString(body, offset);
        offset += serviceType.length() + 1;

        source = new Address();
        offset = source.fromBytes(converter, body, offset, -1);

        destination = new Address();
        offset = destination.fromBytes(converter, body, offset, -1);

        // ESM class, protocol Id, priorityFlag...
        esmClass = (int)converter.readByte(body, offset++);

        // Registered delivery, data coding
        registered = (int)converter.readByte(body, offset++);
        dataCoding = (int)converter.readByte(body, offset++);

        return offset;
    }
}

package org.wayr.smpp.messages;

import java.text.MessageFormat;
import org.wayr.smpp.Address;
import org.wayr.smpp.GSMConstants;
import org.wayr.smpp.GlobalConfiguration;
import org.wayr.smpp.Packet;
import org.wayr.smpp.exceptions.InvalidDateFormatException;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.utils.SMPPDate;

/**
 *
 * @author paul
 */
public class DeliverSMRequest extends SMPPRequest {

    private static final String SPEC_VIOLATION = "Setting the {0} on a "
            + "deliver_sm is in violation of the SMPP specification";

    public DeliverSMRequest() {
        super(Packet.DELIVER_SM);
    }

    @Override
    public void setDeliveryTime(SMPPDate d) {
        GlobalConfiguration.getInstance().getLogger().warn(MessageFormat.format(
                SPEC_VIOLATION, new Object[]{"delivery time"}));
        super.setDeliveryTime(d);
    }

    @Override
    public void setExpiryTime(SMPPDate d) {
        GlobalConfiguration.getInstance().getLogger().warn(MessageFormat.format(
                SPEC_VIOLATION, new Object[]{"expiry time"}));
        super.setExpiryTime(d);
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
        int smLength = 0;
        if (message != null) {
            smLength = message.length;
        }

        int len = this.getBodyLength();
        byte[] buffer = new byte[len];

        int offset = 0;
        offset = converter.writeCString(buffer, offset, this.serviceType);

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

        offset = converter.writeByte(buffer, offset, this.esmClass);
        offset = converter.writeByte(buffer, offset, this.protocolID);
        offset = converter.writeByte(buffer, offset, this.priority);
        offset = converter.writeCString(buffer, offset, dt);
        offset = converter.writeCString(buffer, offset, et);
        offset = converter.writeByte(buffer, offset, this.registered);
        offset = converter.writeByte(buffer, offset, this.replaceIfPresent);
        offset = converter.writeByte(buffer, offset, this.dataCoding);
        offset = converter.writeByte(buffer, offset, this.defaultMsg);
        offset = converter.writeByte(buffer, offset, smLength);
        if (message != null) {
            converter.writeBytes(buffer, offset, message);
        }

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) throws SMPPProtocolException {
        try {
            serviceType = converter.readCString(body, offset);
            offset += serviceType.length() + 1;

            source = new Address();
            offset = source.fromBytes(converter, body, offset, -1);

            destination = new Address();
            offset = destination.fromBytes(converter, body, offset, -1);

            esmClass = (int)converter.readByte(body, offset++);
            protocolID = (int) converter.readByte(body, offset++);
            priority = (int) converter.readByte(body, offset++);

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
            replaceIfPresent = (int)converter.readByte(body, offset++);
            dataCoding = (int)converter.readByte(body, offset++);
            defaultMsg = (int)converter.readByte(body, offset++);
            int smLength = (int)converter.readByte(body, offset++);

            if (smLength > 0) {
                message = converter.readBytes(body, offset, smLength);
                //message = new byte[smLength];
                //System.arraycopy(body, offset, message, 0, smLength);
            }

            return offset + smLength;
        } catch (InvalidDateFormatException e) {
            throw new SMPPProtocolException("Unrecognized date format", e);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 37 * hash + (this.destination != null ? this.destination.hashCode() : 0);
        hash = 37 * hash + (this.message != null ? this.message.hashCode() : 0);
        hash = 37 * hash + this.sequenceNumber;
        hash = 37 * hash + (this.serviceType != null ? this.serviceType.hashCode() : 0);

        return hash;
    }
}

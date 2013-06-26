package org.wayr.smpp.messages;

import java.io.IOException;
import org.wayr.smpp.exceptions.InvalidParameterValueException;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.exceptions.VersionException;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.version.SMPPVersion;

/**
 *
 * @author paul
 */
public class BindRequest extends SMPPRequest {

    /**
     * System Id
     */
    private String sysId;
    /**
     * Authentication password
     */
    private String password;
    /**
     * System type
     */
    private String sysType;
    /**
     * Address range for message routing
     */
    private String addressRange;
    /**
     * Address Type Of Number for message routing
     */
    private int addrTon;
    /**
     * Address Numbering Plan Indicator for message routing
     */
    private int addrNpi;

    public BindRequest(int id) {
        super(id);
    }

    /**
     * Set the system Id
     *
     * @param sysId The System Id to use (Up to 15 characters)
     * @throws InvalidParameterValueException
     */
    public void setSystemId(String sysId) throws InvalidParameterValueException {
        if (sysId != null) {
            if (version.validateSystemId(sysId)) {
                this.sysId = sysId;
            } else {
                throw new InvalidParameterValueException("Invalid system ID", sysId);
            }
        } else {
            this.sysId = null;
        }
    }

    /**
     * Set the password for this transmitter
     *
     * @param password The new password to use (Up to 8 characters in length)
     * @throws InvalidParameterValueException
     */
    public void setPassword(String password) throws InvalidParameterValueException {
        if (password != null) {
            if (version.validatePassword(password)) {
                this.password = password;
            } else {
                throw new InvalidParameterValueException("Invalid password", password);
            }
        } else {
            this.password = null;
        }
    }

    /**
     * Set the system type for this transmitter
     *
     * @param sysType The new system type (Up to 12 characters in length)
     * @throws InvalidParameterValueException
     */
    public void setSystemType(String sysType) throws InvalidParameterValueException {
        if (sysType != null) {
            if (version.validateSystemType(sysType)) {
                this.sysType = sysType;
            } else {
                throw new InvalidParameterValueException("Invalid system type:" + sysType, sysType);
            }
        } else {
            this.sysType = null;
        }
    }

    /**
     * Set the message routing Ton for this transmitter
     *
     * @param addrTon The new Type Of Number to use
     * @throws InvalidParameterValueException
     */
    public void setAddressTon(int addrTon) throws InvalidParameterValueException {
        this.addrTon = addrTon;
    }

    /**
     * Set the message routing Npi for this transmitter
     *
     * @param addrNpi The new Numbering plan indicator to use
     * @throws InvalidParameterValueException
     */
    public void setAddressNpi(int addrNpi) throws InvalidParameterValueException {
        this.addrNpi = addrNpi;
    }

    /**
     * Set the message routing address range for this transmitter
     *
     * @param addressRange The new address range to use (Up to 40 characters)
     * @throws InvalidParameterValueException
     */
    public void setAddressRange(String addressRange) throws InvalidParameterValueException {
        if (addressRange != null) {
            if (version.validateAddressRange(addressRange)) {
                this.addressRange = addressRange;
            } else {
                throw new InvalidParameterValueException("Invalid address range", addressRange);
            }
        } else {
            this.addressRange = null;
        }
    }

    /**
     * Get the system Id
     */
    public String getSystemId() {
        return sysId;
    }

    /**
     * Get the authentication password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get the current system type
     */
    public String getSystemType() {
        return sysType;
    }

    /**
     * Get the routing address regular expression
     */
    public String getAddressRange() {
        return addressRange;
    }

    /**
     * Get the Type of number
     */
    public int getAddressTon() {
        return addrTon;
    }

    /**
     * Get the Numbering plan indicator
     */
    public int getAddressNpi() {
        return addrNpi;
    }

    /**
     * Return the number of bytes this packet would be encoded as to an
     * OutputStream.
     *
     * @return the number of bytes this packet would encode as.
     */
    @Override
    public int getBodyLength() {
        // Calculated as the size of the header plus 3 1-byte ints and
        // 4 null-terminators for the strings plus the length of the strings
        int len = ((sysId != null) ? sysId.length() : 0)
                + ((password != null) ? password.length() : 0)
                + ((sysType != null) ? sysType.length() : 0)
                + ((addressRange != null) ? addressRange.length() : 0);

        // 3 1-byte integers, 4 c-strings
        return len + 3 + 4;
    }

    /**
     * Write the byte representation of this packet to an OutputStream.
     *
     * @param out The output stream to write to
     * @throws IOException
     */
    @Override
    public byte[] toBytes(IOStreamConverter converter) {

        int len = this.getBodyLength();
        byte[] buffer = new byte[len];

        int offset = 0;
        offset = converter.writeCString(buffer, offset, this.sysId);
        offset = converter.writeCString(buffer, offset, this.password);
        offset = converter.writeCString(buffer, offset, this.sysType);
        offset = converter.writeByte(buffer, offset, this.version.getVersionID());
        offset = converter.writeByte(buffer, offset, this.addrTon);
        offset = converter.writeByte(buffer, offset, this.addrNpi);
        converter.writeCString(buffer, offset, this.addressRange);

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) throws SMPPProtocolException {
        try {
            sysId = converter.readCString(body, offset);
            offset += sysId.length() + 1;

            password = converter.readCString(body, offset);
            offset += password.length() + 1;

            sysType = converter.readCString(body, offset);
            offset += sysType.length() + 1;

            int interfaceVer = (int) converter.readByte(body, offset++);

            version = SMPPVersion.getVersion(interfaceVer);
            addrTon = (int)converter.readByte(body, offset++);
            addrNpi = (int)converter.readByte(body, offset++);
            addressRange = converter.readCString(body, offset);
            return offset += addressRange.length() + 1;

        } catch (VersionException e) {
            throw new SMPPProtocolException("Invalid interface version in response", e);
        }
    }
}

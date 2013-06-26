package org.wayr.smpp;

import java.io.Serializable;
import org.wayr.smpp.utils.BytesSerializable;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class Address implements Serializable, BytesSerializable {

    static final long serialVersionUID = -2052018998490218103L;
    /**
     * Type of number.
     */
    private int ton = GSMConstants.GSM_TON_UNKNOWN;
    /**
     * Numbering plan indicator.
     */
    private int npi = GSMConstants.GSM_NPI_UNKNOWN;
    private String address = "";

    /**
     * Create a new Address with all nul values. TON will be 0, NPI will be 0
     * and the address field will be blank.
     */
    public Address() {
    }

    /**
     * Create a new Address.
     *
     * @param ton The Type Of Number.
     * @param npi The Numbering Plan Indicator.
     * @param address The address.
     */
    public Address(int ton, int npi, String address) {
        this.ton = ton;
        this.npi = npi;
        this.address = address;
    }

    /**
     * Get the Type Of Number.
     *
     * @return int
     */
    public int getTON() {
        return ton;
    }

    /**
     * Set the Type of Number.
     *
     * @param int type of number
     */
    public void setTON(int ton) {
        this.ton = ton;
    }

    /**
     * Get the Numbering Plan Indicator.
     *
     * @return int Numbering plan indicator
     */
    public int getNPI() {
        return npi;
    }

    /**
     * Set the Numbering Plan Indicator.
     *
     * @param int Numbering plan indicator
     */
    public void setNPI(int npi) {
        this.npi = npi;
    }

    /**
     * Get the address.
     *
     * @return Address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address.
     *
     * @param Address
     */
    public void setAddress(String address) {
        this.address = (address != null) ? address : "";
    }

    /**
     * Get a unique hash code for this address.
     */
    @Override
    public int hashCode() {
        StringBuilder buf = new StringBuilder();
        buf.append(Integer.toString(ton)).append(':');
        buf.append(Integer.toString(npi)).append(':');
        if (address != null) {
            buf.append(address);
        }

        return buf.hashCode();
    }

    /**
     * Test for equality. Two address objects are equal if their TON, NPI and
     * address fields are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Address) {
            Address a = (Address) obj;
            return (a.ton == ton) && (a.npi == npi) && (a.address.equals(address));
        } else {
            return false;
        }
    }

    /**
     * Get the number of bytes this object would encode to.
     */
    public int getLength() {
        return 3 + address.length();
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getLength()];

        int offset = 0;
        offset = converter.writeByte(buffer, offset, this.ton);
        offset = converter.writeByte(buffer, offset, this.npi);
        converter.writeCString(buffer, offset, this.address);

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] buffer, int offset, int length) {

        this.ton = (int) converter.readByte(buffer, offset++);
        this.npi = (int) converter.readByte(buffer, offset++);
        this.address = converter.readCString(buffer, offset);
        offset += this.address.length() + 1;

        return offset;
    }

    @Override
    public String toString() {
        return new StringBuffer(25).append(Integer.toString(ton)).append(':')
                .append(Integer.toString(npi)).append(':').append(address).toString();
    }
}

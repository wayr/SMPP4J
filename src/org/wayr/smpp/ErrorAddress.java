package org.wayr.smpp;

import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class ErrorAddress extends Address {

    protected int error;

    public ErrorAddress() {
    }

    public ErrorAddress(int ton, int npi, String address) {
        super(ton, npi, address);
    }

    public ErrorAddress(int ton, int npi, String address, int error) {
        super(ton, npi, address);
        this.error = error;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public int getLength() {
        return super.getLength() + 4;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getLength()];
        int offset = 0;
        offset = converter.writeBytes(buffer, offset, super.toBytes(converter));
        converter.writeInt(buffer, offset, error);

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] buffer, int offset, int length) {

        offset = super.fromBytes(converter, buffer, offset, length);
        error = converter.readInt(buffer, offset);

        return offset + 4;
    }
}

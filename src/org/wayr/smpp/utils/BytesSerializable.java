package org.wayr.smpp.utils;

/**
 *
 * @author paul
 */
public interface BytesSerializable {

    public byte[] toBytes(IOStreamConverter converter);

    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length);
}

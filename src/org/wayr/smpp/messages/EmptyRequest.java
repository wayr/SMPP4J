package org.wayr.smpp.messages;

import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class EmptyRequest extends SMPPRequest {

    public EmptyRequest(int id) {
        super(id);
    }

    @Override
    public int getBodyLength() {
        return 0;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        return new byte[0];
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] buffer, int offset, int length) {
        return offset;
    }
}

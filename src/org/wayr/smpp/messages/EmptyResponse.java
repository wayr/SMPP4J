package org.wayr.smpp.messages;

import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class EmptyResponse extends SMPPResponse {

    public EmptyResponse(int id) {
        super(id);
    }

    public EmptyResponse(SMPPRequest q) {
        super(q);
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

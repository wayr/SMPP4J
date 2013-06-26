package org.wayr.smpp.messages;

import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class SubmitSMResponse extends SMPPResponse {

    public SubmitSMResponse() {
        super(SUBMIT_SM_RESP);
    }

    public SubmitSMResponse(SubmitSMRequest q) {
        super(q);
    }

    @Override
    public int getBodyLength() {
        return ((messageId != null) ? messageId.length() : 0) + 1;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] body = new byte[this.getBodyLength()];

        converter.writeCString(body, 0, this.getMessageId());
        return body;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] b, int offset, int length) throws SMPPProtocolException {
        this.messageId = converter.readCString(b, offset);
        return offset + this.messageId.length() + 1;
    }
}

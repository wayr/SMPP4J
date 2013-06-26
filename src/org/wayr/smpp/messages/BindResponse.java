package org.wayr.smpp.messages;

import org.wayr.smpp.exceptions.InvalidParameterValueException;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class BindResponse extends SMPPResponse {

    protected String sysId;

    public BindResponse(int id) {
        super(id);
    }

    public BindResponse(BindRequest request) {
        super(request);
    }

    public void setSystemId(String sysId) throws InvalidParameterValueException {
        if (sysId != null) {
            if (version.validateSystemId(sysId)) {
                this.sysId = sysId;
            } else {
                throw new InvalidParameterValueException("Invalid system Id", sysId);
            }
        } else {
            this.sysId = null;
        }
    }

    public String getSystemId() {
        return sysId;
    }

    @Override
    public int getBodyLength() {
        return ((sysId != null) ? sysId.length() : 0) + 1;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        int len = this.getBodyLength();
        byte[] buffer = new byte[len];

        converter.writeCString(buffer, 0, this.sysId);

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) throws SMPPProtocolException {
        this.sysId = converter.readCString(body, offset);
        return offset + this.sysId.length() + 1;
    }
}

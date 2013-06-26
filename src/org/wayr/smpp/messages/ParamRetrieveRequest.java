package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;
import org.wayr.smpp.exceptions.InvalidParameterValueException;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class ParamRetrieveRequest extends SMPPRequest {

    protected String paramName;

    public ParamRetrieveRequest() {
        super(Packet.PARAM_RETRIEVE);
    }

    public void setParamName(String paramName) throws InvalidParameterValueException{
        if (paramName == null) {
            this.paramName = null;
            return;
        }
        if (paramName.length() < 32) {
            this.paramName = paramName;
        } else {
            throw new InvalidParameterValueException("Parameter name is invalid", this.paramName);
        }

    }

    public String getParamName() {
        return paramName;
    }

    @Override
    public int getBodyLength() {
        return ((paramName != null) ? paramName.length() : 0) + 1;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getBodyLength()];

        converter.writeCString(buffer, 0, this.paramName);
        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] buffer, int offset, int length) {
        this.paramName = converter.readCString(buffer, offset);
        return offset + this.paramName.length() + 1;
    }
}

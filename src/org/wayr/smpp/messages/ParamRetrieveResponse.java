package org.wayr.smpp.messages;

import org.wayr.smpp.Packet;
import org.wayr.smpp.exceptions.InvalidParameterValueException;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class ParamRetrieveResponse extends SMPPResponse {

    protected String paramValue = null;

    public ParamRetrieveResponse() {
        super(Packet.PARAM_RETRIEVE_RESP);
    }

    public ParamRetrieveResponse(ParamRetrieveRequest q) {
        super(q);
    }

    public void setParamValue(String v)  throws InvalidParameterValueException{
        if (v == null) {
            this.paramValue = null;
            return;
        }

        if (v.length() < 101) {
            this.paramValue = v;
        } else {
            throw new InvalidParameterValueException("Parameter value is too long", paramValue);
        }
    }

    public String getParamValue() {
        return paramValue;
    }

    @Override
    public int getBodyLength() {
        return ((paramValue != null) ? paramValue.length() : 0) + 1;
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getBodyLength()];

        converter.writeCString(buffer, 0, this.paramValue);
        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] buffer, int offset, int length) {
        this.paramValue = converter.readCString(buffer, offset);
        return offset + this.paramValue.length() + 1;
    }
}

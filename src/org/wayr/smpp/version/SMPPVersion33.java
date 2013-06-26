package org.wayr.smpp.version;

import org.wayr.smpp.Address;
import org.wayr.smpp.Packet;
import org.wayr.smpp.alphabet.AlphabetEncoding;
import org.wayr.smpp.version.SMPPVersion;

/**
 *
 * @author paul
 */
public class SMPPVersion33 extends SMPPVersion {

    private static final int MAX_MSG_LENGTH = 140;

    SMPPVersion33() {
        super(0x33, "SMPP version 3.3");
    }

    public int getMaxLength(int field) {
        switch (field) {
        case MESSAGE_PAYLOAD:
            return 140;

        default:
            return Integer.MAX_VALUE;
        }
    }

    public boolean validateAddress(Address s) {
        int ton = s.getTON();
        int npi = s.getNPI();
        boolean tonValid = ton >= 0 && ton <= 0xff;
        boolean npiValid = npi >= 0 && npi <= 0xff;
        boolean addressValid = s.getAddress().length() <= 20;
        return tonValid && npiValid && addressValid;
    }

    @Override
    public boolean isSupported(int commandID) {
        switch (commandID & 0x7fffffff) {
        case Packet.BIND_TRANSCEIVER:
        case Packet.DATA_SM:
        case Packet.ALERT_NOTIFICATION:
            return false;

        default:
            return true;
        }
    }

    @Override
    public boolean isSupportOptionalParams() {
        return false;
    }

    @Override
    public boolean validateEsmClass(int c) {
        return c >= 0 && c <= 0xff;
    }

    @Override
    public boolean validateProtocolID(int id) {
        return id >= 0 && id <= 0xff;
    }

    @Override
    public boolean validateDataCoding(int dc) {
        return dc >= 0 && dc <= 0xff;
    }

    @Override
    public boolean validateDefaultMsg(int id) {
        return id >= 0 && id <= 0xff;
    }

    @Override
    public boolean validateMessageText(String text, AlphabetEncoding alphabet) {
        if (text != null) {
            return alphabet.encodeString(text).length <= MAX_MSG_LENGTH;
        } else {
            return true;
        }
    }

    @Override
    public boolean validateServiceType(String type) {
        return type.length() <= 5;
    }

    @Override
    public boolean validateMessageId(String id) {
        try {
            Long.parseLong(id, 16);
            return id.length() <= 8;
        } catch (NumberFormatException x) {
            return false;
        }
    }

    @Override
    public boolean validateMessageState(int state) {
        return state >= 0 && state <= 0xff;
    }

    @Override
    public boolean validateErrorCode(int code) {
        return code >= 0 && code <= 0xff;
    }

    @Override
    public boolean validatePriorityFlag(int flag) {
        return flag == 0 || flag == 1;
    }

    @Override
    public boolean validateRegisteredDelivery(int flag) {
        return flag == 0 || flag == 1;
    }

    @Override
    public boolean validateReplaceIfPresent(int flag) {
        return flag == 0 || flag == 1;
    }

    @Override
    public boolean validateNumberOfDests(int num) {
        return num >= 0 && num <= 255;
    }

    @Override
    public boolean validateNumUnsuccessful(int num) {
        return num >= 0 && num <= 255;
    }

    @Override
    public boolean validateDistListName(String name) {
        return name.length() <= 20;
    }

    @Override
    public boolean validateSystemId(String sysId) {
        return sysId.length() <= 15;
    }

    @Override
    public boolean validatePassword(String password) {
        return password.length() <= 8;
    }

    @Override
    public boolean validateSystemType(String sysType) {
        return sysType.length() <= 12;
    }

    @Override
    public boolean validateAddressRange(String addressRange) {
        return addressRange.length() <= 40;
    }

    @Override
    public boolean validateParamName(String paramName) {
        return paramName.length() <= 31;
    }

    @Override
    public boolean validateParamValue(String paramValue) {
        return paramValue.length() <= 100;
    }

}

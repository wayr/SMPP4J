package org.wayr.smpp.version;

import org.wayr.smpp.Address;
import org.wayr.smpp.alphabet.AlphabetEncoding;
import org.wayr.smpp.exceptions.VersionException;

/**
 *
 * @author paul
 */
public abstract class SMPPVersion {

    public static final int MESSAGE_PAYLOAD = 5;

    public static final SMPPVersion v33 = new SMPPVersion33();
    public static final SMPPVersion v34 = new SMPPVersion34();
    public static SMPPVersion defaultVersion = new SMPPVersion34();

    protected int versionId;
    protected String versionString;

    protected SMPPVersion(int versionId, String versionString) {
        this.versionId = versionId;
        this.versionString = versionString;
    }

    public static SMPPVersion getDefaultVersion() {
        return defaultVersion;
    }

    public static void setDefaultVersion(SMPPVersion version) {
        defaultVersion = version;
    }

    /**
     * Get the integer value for this protocol version object.
     */
    public int getVersionID() {
        return versionId;
    }

    public boolean isOlder(SMPPVersion version) {
        return version.versionId < this.versionId;
    }

    public boolean isNewer(SMPPVersion version) {
        return version.versionId > this.versionId;
    }

    public static SMPPVersion getVersion(int versionId){
        if (versionId == v33.getVersionID()) {
            return v33;
        } else if (versionId == v34.getVersionID()) {
            return v34;
        }
        throw new VersionException("Unknown version id: 0x" + Integer.toHexString(versionId));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SMPPVersion) {
            return ((SMPPVersion) o).versionId == this.versionId;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return new Integer(this.versionId).hashCode();
    }

    @Override
    public String toString() {
        return versionString;
    }

    public abstract int getMaxLength(int field);

    public abstract boolean validateAddress(Address s);

    //--

    public abstract boolean isSupported(int commandID);

    public abstract boolean isSupportOptionalParams();

    public abstract boolean validateEsmClass(int c);

    public abstract boolean validateProtocolID(int id);

    public abstract boolean validateDataCoding(int dc);

    public abstract boolean validateDefaultMsg(int id);

    //--

    public abstract boolean validateMessageText(String text, AlphabetEncoding alphabet);

    //--

    public abstract boolean validateServiceType(String type);

    public abstract boolean validateMessageId(String id);

    public final boolean validateMessageStatus(int st) {
        return this.validateMessageState(st);
    }

    public abstract boolean validateMessageState(int state);

    //--

    public abstract boolean validateErrorCode(int code);

    public abstract boolean validatePriorityFlag(int flag);

    public abstract boolean validateRegisteredDelivery(int flag);

    public abstract boolean validateReplaceIfPresent(int flag);

    public abstract boolean validateNumberOfDests(int num);

    public abstract boolean validateNumUnsuccessful(int num);

    public abstract boolean validateDistListName(String name);

    //--
    public abstract boolean validateSystemId(String sysId);

    public abstract boolean validatePassword(String password);

    public abstract boolean validateSystemType(String sysType);

    public abstract boolean validateAddressRange(String addressRange);

    public abstract boolean validateParamName(String paramName);

    public abstract boolean validateParamValue(String paramValue);

}

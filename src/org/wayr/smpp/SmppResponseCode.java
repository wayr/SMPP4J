package org.wayr.smpp;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author paul
 */
public enum SmppResponseCode {

    OK(0x00000000, "No Error", true),
    INVMSGLEN(0x00000001, "Message too long"),
    INVCMDLEN(0x00000002, "Command length is invalid"),
    INVCMDID(0x00000003, "Command ID is invalid or not supported"),
    INVBNDSTS(0x00000004, "Incorrect bind status for given command"),
    ALYBND(0x00000005, "Already bound"),
    INVPRTFLG(0x00000006, "Invalid Priority Flag"),
    INVREGDLVFLG(0x00000007, "Invalid registered delivery flag"),
    SYSERR(0x00000008, "System error", false, true),
    INVSRCADR(0x0000000A, "Invalid source address"),
    INVDSTADR(0x0000000B, "Invalid destination address"),
    INVMSGID(0x0000000C, "Message ID is invalid"),
    BINDFAIL(0x0000000D, "Bind failed", false, true),
    INVPASWD(0x0000000E, "Invalid password", false, true),
    INVSYSID(0x0000000F, "Invalid System ID", false, true),
    CANCELFAIL(0x00000011, "Cancelling message failed"),
    REPLACEFAIL(0x00000013, "Message recplacement failed"),
    MSSQFUL(0x00000014, "Message queue full", false, true),
    INVSERTYP(0x00000015, "Invalid service type"),
    INVNUMDESTS(0x00000033, "Invalid number of destinations"),
    INVDLNAME(0x00000034, "Invalid distribution list name"),
    INVDESTFLAG(0x00000040, "Invalid destination flag"),
    INVSUBREP(0x00000042, "Invalid submit with replace request"),
    INVESMCLASS(0x00000043, "Invalid esm class set"),
    CNTSUBDL(0x00000044, "Invalid submit to ditribution list"),
    SUBMITFAIL(0x00000045, "Submitting message has failed"),
    INVSRCTON(0x00000048, "Invalid source address type of number ( TON )"),
    INVSRCNPI(0x00000049, "Invalid source address numbering plan ( NPI )"),
    INVDSTTON(0x00000050, "Invalid destination address type of number ( TON )"),
    INVDSTNPI(0x00000051, "Invalid destination address numbering plan ( NPI )"),
    INVSYSTYP(0x00000053, "Invalid system type"),
    INVREPFLAG(0x00000054, "Invalid replace_if_present flag"),
    INVNUMMSGS(0x00000055, "Invalid number of messages"),
    THROTTLED(0x00000058, "Throttling error"),
    INVSCHED(0x00000061, "Invalid scheduled delivery time"),
    INVEXPIRY(0x00000062, "Invalid Validty Period value"),
    INVDFTMSGID(0x00000063, "Predefined message not found"),
    X_T_APPN(0x00000064, "ESME Receiver temporary error", false, true),
    X_P_APPN(0x00000065, "ESME Receiver permanent error", false, true),
    X_R_APPN(0x00000066, "ESME Receiver reject message error", false, true),
    QUERYFAIL(0x00000067, "Message query request failed", false, true),
    INVTLVSTREAM(0x000000C0, "Error in the optional part of the PDU body"),
    TLVNOTALLWD(0x000000C1, "TLV not allowed"),
    INVTLVLEN(0x000000C2, "Invalid parameter length"),
    MISSINGTLV(0x000000C3, "Expected TLV missing"),
    INVTLVVAL(0x000000C4, "Invalid TLV value"),
    DELIVERYFAILURE(0x000000FE, "Transaction delivery failure"),
    UNKNOWN(0x000000FF, "Unknown error"),
    SERTYPUNAUTH(0x00000100, "ESME not authorised to use specified servicetype"),
    PROHIBITED(0x00000101, "ESME prohibited from using specified operation"),
    SERTYPUNAVAIL(0x00000102, "Specified servicetype is unavailable"),
    SERTYPDENIED(0x00000103, "Specified servicetype is denied"),
    INVDCS(0x00000104, "Invalid data coding scheme"),
    INVSRCADDRSUBUNIT(0x00000105, "Invalid source address subunit"),
    INVSTDADDRSUBUNIR(0x00000106, "Invalid destination address subunit"),
    INVBALANCE(0x0000040B, "Insufficient credits to send message", false, true),
    UNESME_SPRTDDESTADDR(0x0000040C, "Destination address blocked by the ActiveXperts SMPP Demo Server");
    private final int value;
    private final boolean ok;
    private final boolean retry;
    private final String description;
    private static final Map<Integer, SmppResponseCode> lookup = new HashMap<Integer, SmppResponseCode>();

    static {
        for (SmppResponseCode s : EnumSet.allOf(SmppResponseCode.class)) {
            lookup.put(s.getValue(), s);
        }
    }

    private SmppResponseCode(int value, String description) {
        this(value, description, false, false);
    }

    private SmppResponseCode(int value, String description, boolean ok) {
        this(value, description, ok, false);
    }

    private SmppResponseCode(int value, String description, boolean ok, boolean retry) {
        this.value = value;
        this.ok = ok;
        this.retry = retry;
        this.description = description;
    }

    public int getValue() {
        return this.value;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOk() {
        return ok;
    }

    public boolean needRetry() {
        return retry;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + Integer.toHexString(value) + ",\"" + this.description  + "\", " + ok + ", " + retry + ")";
    }

    public static SmppResponseCode get(Integer code) {
        if (!lookup.containsKey(code)) {
            return SmppResponseCode.UNKNOWN;
        }
        return lookup.get(code);
    }
}

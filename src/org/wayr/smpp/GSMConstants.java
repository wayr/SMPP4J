package org.wayr.smpp;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author paul
 */

public final class GSMConstants {
    /** No error */
    public static final int GSM_ERR_NONE = 0;

    /** Customer unknown */
    public static final int GSM_ERR_P_UNKNOWN = 1;

    /** Not provisioned */
    public static final int GSM_ERR_P_PROVISION = 11;

    /** Call barred */
    public static final int GSM_ERR_T_BARRED = 13;

    /** CUG reject */
    public static final int GSM_ERR_P_CUG = 15;

    /** SMS not supported by MS */
    public static final int GSM_ERR_T_MSSUPPORT = 19;

    /** error in receiving MS */
    public static final int GSM_ERR_T_MSERROR = 20;

    /** facility not supported */
    public static final int GSM_ERR_T_SUPPORT = 21;

    /** memory capacity exceeded */
    public static final int GSM_ERR_T_MEMCAP = 22;

    /** absent subscriber */
    public static final int GSM_ERR_T_ABSENT = 29;

    /** absent subscriber detached */
    public static final int GSM_ERR_T_ABSENT_DETACHED = 30;

    /** MS didn't respond to 2 pages */
    public static final int GSM_ERR_T_ABSENT_PAGEFAIL = 31;

    /** Subscriber roamed to new PLMN */
    public static final int GSM_ERR_T_SUPPORT_ROAMING = 32;

    /** system failure */
    public static final int GSM_ERR_T_SYSTEM = 36;

    public static final int GSM_TON_UNKNOWN = 0;

    public static final int GSM_TON_INTERNATIONAL = 1;

    public static final int GSM_TON_NATIONAL = 2;

    public static final int GSM_TON_NETWORK = 3;

    public static final int GSM_TON_SUBSCRIBER = 4;

    public static final int GSM_TON_ALPHANUMERIC = 5;

    public static final int GSM_TON_ABBREVIATED = 6;

    public static final int GSM_TON_RESERVED_EXTN = 7;

    public static final int GSM_NPI_UNKNOWN = 0;

    public static final int GSM_NPI_E164 = 1;

    public static final int GSM_NPI_ISDN = GSM_NPI_E164;

    public static final int GSM_NPI_X121 = 3;

    public static final int GSM_NPI_TELEX = 4;

    public static final int GSM_NPI_NATIONAL = 8;

    public static final int GSM_NPI_PRIVATE = 9;

    public static final int GSM_NPI_ERMES = 10;

    public static final int GSM_NPI_RESERVED_EXTN = 15;

    private static Map<Integer, String> errorTable = new HashMap<Integer, String>();

    static {
        GSMConstants.errorTable.put(GSM_ERR_NONE, "No error");
        GSMConstants.errorTable.put(GSM_ERR_P_UNKNOWN, "Unknown Customer");
        GSMConstants.errorTable.put(GSM_ERR_P_PROVISION, "Not provisioned");
        GSMConstants.errorTable.put(GSM_ERR_T_BARRED, "Call is barred");
        GSMConstants.errorTable.put(GSM_ERR_P_CUG, "CUG Rejected");
        GSMConstants.errorTable.put(GSM_ERR_T_MSSUPPORT,"Sms not supported by MS");
        GSMConstants.errorTable.put(GSM_ERR_T_MSERROR, "Error in receiveing MS");
        GSMConstants.errorTable.put(GSM_ERR_T_SUPPORT, "Facility not supported");
        GSMConstants.errorTable.put(GSM_ERR_T_MEMCAP,"Memory capacity exceeded");
        GSMConstants.errorTable.put(GSM_ERR_T_ABSENT, "Absent Subscriber");
        GSMConstants.errorTable.put(GSM_ERR_T_ABSENT_DETACHED,"Absent Subscriber detached");
        GSMConstants.errorTable.put(GSM_ERR_T_ABSENT_PAGEFAIL,"MS did not respond to 2 pages");
        GSMConstants.errorTable.put(GSM_ERR_T_SUPPORT_ROAMING,"Subscriber roamed to new PLMN");
        GSMConstants.errorTable.put(GSM_ERR_T_SYSTEM, "System failure");
    }

    /**
     * Get a string for the GSM error code.
     */
    public static String getGsmErr(int code) {
        String s = GSMConstants.errorTable.get(code);
        return s == null ? "" : s;
    }
}


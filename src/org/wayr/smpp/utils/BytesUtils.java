package org.wayr.smpp.utils;

/**
 *
 * @author paul
 */
public class BytesUtils {

    public static <E> String join(E[] s, String delimiter, int group) {
        if (s == null || s.length <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder("");
        boolean first = true;
        for (int i = 0; i < s.length; i += group) {

            if (!first) {
                builder.append(delimiter);
            } else {
                first = false;
            }

            for (int j = 0; j < group && i + j < s.length; j++) {
                builder.append(s[i + j]);
            }

        }
        return builder.toString();
    }

    public static String[] bytesToHex(byte[] bytes) {
        return bytesToHex(bytes, 0, bytes.length);
    }

    public static String[] bytesToHex(byte[] bytes, int offset, int length) {
        if (length < 0) {
            return new String[0];
        }
        String[] hexChars = new String[length];

        length = Math.min(bytes.length, length);
        int v;
        for (int j = offset; j < length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j] = String.format("%02X", v);
        }
        return hexChars;
    }

    public static String bytesToHexString(byte[] bytes) {
        return bytesToHexString(bytes, 0, bytes.length);
    }

    public static String bytesToHexString(byte[] bytes, int offset, int length) {
        String[] hexChars = bytesToHex(bytes, offset, length);

        return join(hexChars, " ", 1);
    }
}

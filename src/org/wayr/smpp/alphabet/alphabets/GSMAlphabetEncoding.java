package org.wayr.smpp.alphabet.alphabets;

import java.io.ByteArrayOutputStream;
import org.wayr.smpp.alphabet.AlphabetEncoding;

/**
 *
 * @author paul
 */
public class GSMAlphabetEncoding extends AlphabetEncoding{

    private static final int DCS = 0;

    public static final int EXTENDED_ESCAPE = 0x1b;

    public static final int PAGE_BREAK = 0x0a;

    private static final char[] CHAR_TABLE = {
        '@', '\u00a3', '$', '\u00a5', '\u00e8', '\u00e9', '\u00f9', '\u00ec',
        '\u00f2', '\u00c7', '\n', '\u00d8', '\u00f8', '\r', '\u00c5', '\u00e5',
        '\u0394', '_', '\u03a6', '\u0393', '\u039b', '\u03a9', '\u03a0', '\u03a8',
        '\u03a3', '\u0398', '\u039e', ' ', '\u00c6', '\u00e6', '\u00df', '\u00c9',
        ' ', '!', '"', '#', '\u00a4', '%', '&', '\'',
        '(', ')', '*', '+', ',', '-', '.', '/',
        '0', '1', '2', '3', '4', '5', '6', '7',
        '8', '9', ':', ';', '<', '=', '>', '?',
        '\u00a1', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
        'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
        'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
        'X', 'Y', 'Z', '\u00c4', '\u00d6', '\u00d1', '\u00dc', '\u00a7',
        '\u00bf', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
        'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
        'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
        'x', 'y', 'z', '\u00e4', '\u00f6', '\u00f1', '\u00fc', '\u00e0',
    };

    /**
     * Extended character table. Characters in this table are accessed by the
     * 'escape' character in the base table. It is important that none of the
     * 'inactive' characters ever be matchable with a valid base-table
     * character as this breaks the encoding loop.
     *
     * @see #EXTENDED_ESCAPE
     */
    private static final char[] EXT_CHAR_TABLE = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, '^', 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            '{', '}', 0, 0, 0, 0, 0, '\\',
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, '[', '~', ']', 0,
            '|', 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, '\u20ac', 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
    };


    public GSMAlphabetEncoding() {
        super(DCS);
    }


    @Override
    public String decodeString(byte[] b) {
        if (b == null) {
            return "";
        }

        char[] table = CHAR_TABLE;
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < b.length; i++) {
            int code = (int) b[i] & 0x000000ff;
            if (code == EXTENDED_ESCAPE) {
                table = EXT_CHAR_TABLE;
            } else {
                buf.append((code >= table.length) ? '?' : table[code]);
                table = CHAR_TABLE;
            }
        }

        return buf.toString();
    }


    @Override
    public byte[] encodeString(String s) {
        if (s == null) {
            return new byte[0];
        }

        char[] c = s.toCharArray();
        ByteArrayOutputStream enc = new ByteArrayOutputStream(256);

        for (int loop = 0; loop < c.length; loop++) {
            int search = 0;
            for (; search < CHAR_TABLE.length; search++) {
                if (search == EXTENDED_ESCAPE) {
                    continue;
                }

                if (c[loop] == CHAR_TABLE[search]) {
                    enc.write((byte) search);
                    break;
               }

                if (c[loop] == EXT_CHAR_TABLE[search]) {
                    enc.write((byte) EXTENDED_ESCAPE);
                    enc.write((byte) search);
                    break;
               }
            }
            if (search == CHAR_TABLE.length) {
                // A '?' character.
                enc.write(0x3f);
            }
        }

        return enc.toByteArray();
    }

    @Override
    public int getEncodingLength() {
        return 7;
    }

    public byte[] pack(byte[] unpacked) {
        int packedLen = unpacked.length - (unpacked.length / 8);
        byte[] packed = new byte[packedLen];
        int pos = 0;
        int i = 0;
        while (i < unpacked.length) {
            int jmax = (i + 7) > unpacked.length ? unpacked.length - i : 7;
            int mask = 0x1;
            for (int j = 0; j < jmax; j++) {
                int b1 = (int) unpacked[i + j] & 0xff;
                int b2 = 0x0;
                try {
                    b2 = (int) unpacked[i + j + 1] & mask;
                } catch (ArrayIndexOutOfBoundsException x) {
                }
                packed[pos++] = (byte) ((b1 >>> j) | (b2 << (8 - (j + 1))));
                mask = (mask << 1) | 1;
            }
            i += 8;
        }
        return packed;
    }

    public byte[] unpack(byte[] packed) {
        int unpackedLen = (packed.length * 8) / 7;
        byte[] unpacked = new byte[unpackedLen];
        int pos = 0;
        int i = 0;
        while (i < packed.length) {
            int mask = 0x7f;
            int jmax = (i + 8) > packed.length ? (packed.length - i) : 8;

            for (int j = 0; j < jmax; j++) {
                int b1 = (int) packed[i + j] & mask;
                int b2 = 0x0;
                try {
                    b2 = (int) packed[(i + j) - 1] & 0x00ff;
                } catch (ArrayIndexOutOfBoundsException x) {
                }
                unpacked[pos++] = (byte) ((b1 << j) | (b2 >>> (8 - j)));
                mask >>= 1;
            }
            i += 7;
        }
        return unpacked;
    }

    @Override
    public String toString() {
        final String fmt = "{0,number,000}: {1}  ";
        final Object[] args = new Object[2];

        StringBuilder b = new StringBuilder(256);
        b.append("Table size: ").append(CHAR_TABLE.length).append('\n');
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 8; j++) {
                int pos = i + (16 * j);
                args[0] = new Integer(pos);

                if (CHAR_TABLE[pos] == '\r') {
                    args[1] = "CR";
                } else if (CHAR_TABLE[pos] == '\n') {
                    args[1] = "LF";
                } else if (CHAR_TABLE[pos] == ' ') {
                    args[1] = "SP";
                } else {
                    args[1] = " " + CHAR_TABLE[pos];
                }
                b.append(java.text.MessageFormat.format(fmt, args));
            }
            b.append('\n');
        }
        return b.toString();
    }

    @Override
    public int getMaxLength() {
        return 160;
    }

    @Override
    public int getSplitLength() {
        return 152;
    }
}

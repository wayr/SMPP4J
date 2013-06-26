package org.wayr.smpp.utils;

/**
 *
 * @author paul
 */
public abstract class NumberSignificantBitConverter {

    /**
     *
     * @param b the bytes array
     * @param offset the array index of the int representation
     * @param size the number of bytes to convert into the integer
     * @return the int value
     */
    public abstract int toInt(byte[] b, int offset, int size);

    /**
     *
     * @param b the bytes array
     * @param offset the array index of the long representation
     * @param size the number of bytes to convert into the long
     * @return the long value
     */
    public abstract long toLong(byte[] b, int offset, int size);

    /**
     *
     * @param b the bytes array
     * @param offset the array index of the long representation
     * @param size the number of bytes to convert into the long
     * @return the long value
     */
    public abstract Number toNumber(byte[] b, int offset, int size);

    public byte[] convertFromInt(int num, int len) {
        return this.convertFromInt(num, len, null, 0);
    }

    public byte[] convertFromLong(long num, int len) {
        return this.convertFromLong(num, len, null, 0);
    }

    public byte[] convertFromNumber(Number num, int len) {
        return this.convertFromNumber(num, len, null, 0);
    }

    public abstract byte[] convertFromInt(int num, int len, byte[] array, int offset);

    public abstract byte[] convertFromLong(long num, int len, byte[] array, int offset);

    public abstract byte[] convertFromNumber(Number num, int len, byte[] array, int offset);
}

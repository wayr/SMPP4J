package org.wayr.smpp.utils;

/**
 *
 * @author paul
 */
public class MSBNumberToByteConverter extends NumberSignificantBitConverter {

    @Override
    public int toInt(byte[] b, int offset, int size) {
        /*int num = 0;
        int sw = 8 * (size - 1);

        for (int loop = 0; loop < size; loop++) {
            num |= ((int) b[offset + loop] & 0x00ff) << sw;
            sw -= 8;
        }

        return num;*/
        return this.toNumber(b, offset, size).intValue();
    }

    @Override
    public long toLong(byte[] b, int offset, int size){
        /*long num = 0;
        long sw = 8L * ((long) size - 1L);

        for (int loop = 0; loop < size; loop++) {
            num |= ((long) b[offset + loop] & 0x00ff) << sw;
            sw -= 8;
        }

        return num;*/
        return this.toNumber(b, offset, size).longValue();
    }

    @Override
    public Number toNumber(byte[] b, int offset, int size) {
        long num = 0;
        long sw = 8L * ((long) size - 1L);

        for (int loop = 0; loop < size; loop++) {
            num |= ((long) b[offset + loop] & 0x00ff) << sw;
            sw -= 8;
        }

        return new Long(num);
    }

    @Override
    public byte[] convertFromInt(int num, int len, byte[] array, int offset) {

        /*byte[] b = array;
        if (array == null) {
            b = new byte[len];
        }

        int sw = (len - 1) * 8;
        int mask = 0xff << sw;

        for (int i = 0; i < len; i++) {
            b[offset + i] = (byte) ((num & mask) >>> sw);

            sw -= 8;
            mask >>>= 8;
        }

        return b;*/
        return this.convertFromNumber(num, len, array, offset);
    }

    @Override
    public byte[] convertFromLong(long num, int len, byte[] array, int offset) {
        /*
        byte[] b = array;
        if (array == null) {
            b = new byte[len];
        }

        long sw = (len - 1) * 8;
        long mask = 0xff << sw;

        for (int i = 0; i < len; i++) {
            b[offset + i] = (byte) ((num & mask) >>> sw);

            sw -= 8;
            mask >>>= 8;
        }

        return b;*/
        return this.convertFromNumber(num, len, array, offset);
    }

    @Override
    public byte[] convertFromNumber(Number num, int len, byte[] array, int offset) {

        byte[] b = array;
        if (array == null) {
            b = new byte[len];
        }

        long sw = (len - 1) * 8;
        long mask = 0xff << sw;

        for (int i = 0; i < len; i++) {
            b[offset + i] = (byte) ((num.longValue() & mask) >>> sw);

            sw -= 8;
            mask >>>= 8;
        }

        return b;
    }
}

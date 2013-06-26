package org.wayr.smpp.utils;

/**
 *
 * @author paul
 */
public class ByteSequence implements SequenceScheme<Byte> {

    protected byte num = 1;

    public ByteSequence() {
    }

    public ByteSequence(Integer start) {
        if (start >= Byte.MAX_VALUE) {
            start = 1;
        }
        num = start.byteValue();
    }

    @Override
    public Byte nextNumber() {
        if (num == Byte.MAX_VALUE) {
            num = 1;
            return Byte.MAX_VALUE;
        } else {
            return num++;
        }
    }

    @Override
    public Byte peek() {
        return num;
    }

    @Override
    public Byte peek(int nth) throws UnsupportedOperationException {
        Integer n = num + nth;
        if (n >= Byte.MAX_VALUE) {
            n = Byte.MAX_VALUE - n;
        }
        return n.byteValue();
    }

    @Override
    public void reset() {
        num = 1;
    }
}

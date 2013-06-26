package org.wayr.smpp.utils;

/**
 *
 * @author paul
 */
public class IntegerSequence implements SequenceScheme<Integer> {

    protected int num = 1;

    public IntegerSequence() {
    }

    public IntegerSequence(int start) {
        this.num = start;
    }

    @Override
    public synchronized Integer nextNumber() {
        if (num == Integer.MAX_VALUE) {
            num = 1;
            return Integer.MAX_VALUE;
        } else {
            return num++;
        }
    }

    @Override
    public synchronized Integer peek() {
        return num;
    }

    @Override
    public synchronized Integer peek(int nth) throws UnsupportedOperationException {
        return num + nth;
    }

    @Override
    public synchronized void reset() {
        num = 1;
    }
}

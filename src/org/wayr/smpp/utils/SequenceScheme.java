package org.wayr.smpp.utils;

/**
 *
 * @author paul
 */
public interface SequenceScheme<E> {

    /**
     * Return the next sequence
     *
     * @return E
     */
    E nextNumber();

    /**
     * Return the current sequence
     *
     * @return E
     */
    E peek();

    /**
     * Return the nth sequence from current
     *
     * @throws
     * @param nth
     * @return
     */
    E peek(int nth) throws UnsupportedOperationException;

    /**
     * Reset the sequence
     */
    void reset();
}

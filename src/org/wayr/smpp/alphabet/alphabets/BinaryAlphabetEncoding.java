package org.wayr.smpp.alphabet.alphabets;

import org.wayr.smpp.alphabet.AlphabetEncoding;

/**
 *
 * @author paul
 */
public class BinaryAlphabetEncoding extends AlphabetEncoding {

    private static final int DCS = 4;

    public BinaryAlphabetEncoding() {
        super(DCS);
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

package org.wayr.smpp.alphabet.alphabets;

import java.io.UnsupportedEncodingException;
import org.wayr.smpp.alphabet.AlphabetEncoding;

/**
 *
 * @author paul
 */
public class Latin1AlphabetEncoding extends AlphabetEncoding {

    private static final int DCS = 3;

    public Latin1AlphabetEncoding() throws UnsupportedEncodingException {
        super(DCS);
        setCharset("ISO-8859-1");
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
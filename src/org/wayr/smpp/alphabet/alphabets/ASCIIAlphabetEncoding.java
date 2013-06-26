package org.wayr.smpp.alphabet.alphabets;

import java.io.UnsupportedEncodingException;
import org.wayr.smpp.alphabet.AlphabetEncoding;

/**
 *
 * @author paul
 */
public class ASCIIAlphabetEncoding extends AlphabetEncoding {

    private static final int DCS = 1;

    public ASCIIAlphabetEncoding() {
        super(DCS);
        try {
            setCharset("US-ASCII");
        } catch (UnsupportedEncodingException x) {
            // All JVMs are required to support ASCII..
        }
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

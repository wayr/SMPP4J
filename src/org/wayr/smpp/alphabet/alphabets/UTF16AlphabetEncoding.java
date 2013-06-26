package org.wayr.smpp.alphabet.alphabets;

import java.io.UnsupportedEncodingException;
import org.wayr.smpp.alphabet.AlphabetEncoding;

/**
 *
 * @author paul
 */
public class UTF16AlphabetEncoding extends AlphabetEncoding {

    private static final int DCS = 8;

    public UTF16AlphabetEncoding(boolean bigEndian) throws UnsupportedEncodingException{
        super(DCS);
        if (!bigEndian) {
            setCharset("UTF-16LE");
        } else {
            setCharset("UTF-16BE");
        }
    }

    @Override
    public int getMaxLength() {
        return 140;
    }

    @Override
    public int getSplitLength() {
        return 132;
    }
}

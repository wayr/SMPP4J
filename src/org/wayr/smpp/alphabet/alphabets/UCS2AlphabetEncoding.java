package org.wayr.smpp.alphabet.alphabets;

import java.io.UnsupportedEncodingException;
import org.wayr.smpp.alphabet.AlphabetEncoding;

/**
 *
 * @author paul
 */
public class UCS2AlphabetEncoding extends AlphabetEncoding {

    private static final int DCS = 8;

    public UCS2AlphabetEncoding() throws UnsupportedEncodingException{
        super(DCS);
        setCharset("ISO-10646-UCS-2");
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

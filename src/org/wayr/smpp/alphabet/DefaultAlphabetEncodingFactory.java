package org.wayr.smpp.alphabet;

import java.io.UnsupportedEncodingException;
import org.wayr.smpp.GlobalConfiguration;
import org.wayr.smpp.alphabet.alphabets.ASCIIAlphabetEncoding;
import org.wayr.smpp.alphabet.alphabets.BinaryAlphabetEncoding;
import org.wayr.smpp.alphabet.alphabets.GSMAlphabetEncoding;
import org.wayr.smpp.alphabet.alphabets.Latin1AlphabetEncoding;
import org.wayr.smpp.alphabet.alphabets.UCS2AlphabetEncoding;

/**
 *
 * @author paul
 */
public class DefaultAlphabetEncodingFactory extends AlphabetEncodingFactory {

    public DefaultAlphabetEncodingFactory() {
        super(new GSMAlphabetEncoding(), "GSM");

        this.addEncoding(new ASCIIAlphabetEncoding(), "ASCII");
        this.addEncoding(new BinaryAlphabetEncoding(), "BINARY");

        try {
            this.addEncoding(new UCS2AlphabetEncoding(), "UCS2");
        } catch (UnsupportedEncodingException xx) {
            logger.warn("JVM does not support UCS2 - encoding will not be used.");
            this.addEncoding(new GSMAlphabetEncoding(), "UCS2");
        }

        try {
            this.addEncoding(new Latin1AlphabetEncoding(), "latin1");
        } catch (UnsupportedEncodingException xx) {
            logger.warn("JVM does not support Latin1 - encoding will not be used.");
            this.addEncoding(new GSMAlphabetEncoding(), "latin1");
        }
    }
}

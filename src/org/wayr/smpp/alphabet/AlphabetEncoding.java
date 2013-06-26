package org.wayr.smpp.alphabet;

import java.io.UnsupportedEncodingException;

/**
 *
 * @author paul
 */
public abstract class AlphabetEncoding {

    protected int dataCoding = -1;
    protected String charset;

    protected AlphabetEncoding(int dataCoding) {
        this.dataCoding = dataCoding;
    }

    /**
     * Get the correct data_coding value for this message encoding type.
     */
    public int getDataCoding() {
        return dataCoding;
    }

    /**
     * Get the number of bits each encoded message byte represents.
     *
     * @return int
     */
    public int getEncodingLength() {
        return 8;
    }

    public abstract int getMaxLength();

    public abstract int getSplitLength();

    /**
     * Set the charset of this alphabet encoding.
     *
     * @param charset
     * @throws UnsupportedEncodingException
     */
    protected void setCharset(String charset) throws UnsupportedEncodingException {
        "test".getBytes(charset);
        this.charset = charset;
    }

    /**
     * Get the character set in use by this alpabet encoding (if any).
     *
     * @return
     */
    public String getCharset() {
        return charset;
    }

    /**
     * Convert byte message text into a String
     * @param b
     * @return
     */
    public String decodeString(byte[] b) {
        if( charset == null ){
            throw new RuntimeException("Missing charset of " + this.getClass().getName());
        }

        try{
            if( b != null ){
                return new String(b, charset);
            }
        } catch ( UnsupportedEncodingException e){

        }
        return "";
    }

    /**
     * Convert a String into a byte message
     * @param s
     * @return
     */
    public byte[] encodeString(String s) {
        if (charset == null) {
            throw new RuntimeException("Missing charset of " + this.getClass().getName());
        }

        try{
            if (s != null) {
                return s.getBytes(charset);
            }
        } catch ( UnsupportedEncodingException e){

        }
        return new byte[0];
    }
}

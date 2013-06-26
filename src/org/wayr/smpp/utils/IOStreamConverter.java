package org.wayr.smpp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author paul
 */
public class IOStreamConverter {

    private static final String US_ASCII = "US-ASCII";
    private static final String UTF8 = "UTF-8";
    protected NumberSignificantBitConverter converter;

    public IOStreamConverter(NumberSignificantBitConverter converter) {
        this.converter = converter;
    }

    public NumberSignificantBitConverter getNumberConverter() {
        return converter;
    }

    public void setNumberConverter(NumberSignificantBitConverter converter) {
        this.converter = converter;
    }

    public byte[] realloc(byte[] source, int len){
        if( len > source.length ){
            byte[] bytes = new byte[len];
            System.arraycopy(source, 0, bytes, 0, source.length);
            return bytes;
        }
        return source;
    }

    public byte[] readBytes(InputStream in, int len) throws IOException {
        byte[] b = new byte[len];
        int p = 0;
        for (int loop = 0; loop < (len - p); loop++) {
            int r = in.read(b, p, len - p);
            if (r == -1) {
                break;
            }

            p += r;
        }

        return b;
    }

    public int readInt(InputStream in, int len) throws IOException {
        /*byte[] b = new byte[len];
        int p = 0;
        for (int loop = 0; loop < (len - p); loop++) {
            int r = in.read(b, p, len - p);
            if (r == -1) {
                break;
            }

            p += r;
        }*/

        return converter.toInt(this.readBytes(in, len), 0, len);
    }

    public byte readByte(byte[] source, int offset) {
        return this.readNumber(source, offset, 1).byteValue();
    }

    public short readShort(byte[] source, int offset) {
        return this.readNumber(source, offset, 2).shortValue();
    }

    public int readInt(byte[] source, int offset) {
        return this.readNumber(source, offset, 4).intValue();
    }

    public long readLong(byte[] source, int offset) {
        return this.readNumber(source, offset, 8).longValue();
    }

    public Number readNumber(byte[] source, int offset, int len) {
        return converter.toNumber(source, offset, len);
    }

    public long readLong(InputStream in, int len) throws IOException {
        /*byte[] b = new byte[len];
        int p = 0;
        for (int loop = 0; loop < (len - p); loop++) {
            int r = in.read(b, p, len - p);
            if (r == -1) {
                break;
            }

            p += r;
        }*/

        return converter.toLong(this.readBytes(in, len), 0, len);
    }

    public String readCString(InputStream in) throws IOException {
        StringBuilder s = new StringBuilder();

        int b = in.read();
        while (b != 0) {
            if (b == -1) {
                throw new IOException("End of Input Stream before NULL byte");
            }

            s.append((char) b);
            b = in.read();
        }

        if (s.length() == 0) {
            return null;
        } else {
            return s.toString();
        }
    }

    public String readCString(byte[] b, int offset) {
        String s;
        try {
            int p = offset;
            while (b[p] != (byte) 0) {
                p++;
            }

            if (p > offset) {
                s = new String(b, offset, p - offset, US_ASCII);
            } else {
                s = "";
            }
        } catch (UnsupportedEncodingException x) {
            s = "";
        }
        return s;
    }

    public void writeCString(String s, OutputStream out)
            throws java.io.IOException {
        this.writeString(s, out);
        out.write((byte) 0);
    }

    public int writeCString(byte[] destination, int offset, String source){
        if( source == null ){
            source = "";
        }
        if( destination.length < offset + source.length() ){
            destination = this.realloc(destination, offset + source.length() + 1);
        }

        //System.arraycopy(destination, offset, b, 0, source.length());
        System.arraycopy(source.getBytes(), 0, destination, offset, source.length());
        offset += source.length();
        destination[offset] = 0;
        offset++;

        return offset;
    }

    public byte[] readBytes(byte[] b, int offset, int len) {
        byte[] buffer = new byte[len];
        System.arraycopy(b, offset, buffer, 0, len);
        return buffer;
    }

    public String readString(byte[] b, int offset, int len) {
        String s = "";
        try {
            if (len > 0) {
                s = new String(b, offset, len - offset, US_ASCII);
            }
        } catch (UnsupportedEncodingException x) {
        }
        return s;
    }

    public String readString(InputStream in, int len) throws IOException {
        String s = null;
        if (len >= 1) {
            byte[] b = new byte[len];
            int l = 0;
            StringBuilder buf = new StringBuilder();

            while (l < len) {
                int r = in.read(b, 0, len - l);
                if (r == -1) {
                    throw new IOException("EOS before NUL byte read.");
                }

                l += r;
                buf.append(new String(b, 0, r, US_ASCII));
            }

            if (buf.length() > 0) {
                s = buf.toString();
            }
        }
        return s;
    }

    public void writeString(String s, int len, OutputStream out) throws IOException {
        if (s == null) {
            return;
        }

        if (len > s.length()) {
            this.writeString(s, out);
        } else {
            this.writeString(s.substring(0, len), out);
        }
    }

    public void writeString(String s, OutputStream out) throws IOException {
        if (s == null) {
            return;
        }
        out.write(s.getBytes());
    }

    public int writeString(byte[] destination, int offset, String source){
        if( destination.length < offset + source.length() ){
            destination = this.realloc(destination, offset + source.length() + 1);
        }

        System.arraycopy(source.getBytes(), 0, destination, offset, source.length());
        offset += source.length();

        return offset;
    }

    public int writeByte(byte[] destination, int offset, Number num){
        return this.writeNumber(destination, offset, num, 1);
    }

    public int writeShort(byte[] destination, int offset, Number num){
        return this.writeNumber(destination, offset, num, 2);
    }

    public int writeInt(byte[] destination, int offset, Number num){
        return this.writeNumber(destination, offset, num, 4);
    }

    public int writeLong(byte[] destination, int offset, Number num){
        return this.writeNumber(destination, offset, num, 4);
    }

    public int writeNumber(byte[] destination, int offset, Number num, int len){
        if( destination.length < offset + len ){
            destination = this.realloc(destination, offset + len);
        }
        byte[] bytes = this.converter.convertFromNumber(num, len);

        System.arraycopy(bytes, 0, destination, offset, len);
        return offset + len;
    }

    /*public int writeLong(byte[] destination, int offset, Long num, int len){
        if( destination.length < offset + len ){
            destination = this.realloc(destination, offset + len);
        }
        byte[] bytes = this.converter.convertFromLong(num, len);

        System.arraycopy(bytes, 0, destination, offset, len);
        return offset + len;
    }*/

    public int writeBytes(byte[] destination, int offsetDestination, byte[] source){
        return this.writeBytes(destination, offsetDestination, source, 0, source.length);
    }

    public int writeBytes(byte[] destination, int offsetDestination, byte[] source, int offsetSource){
        return this.writeBytes(destination, offsetDestination, source, offsetSource, source.length - offsetSource);
    }

    public int writeBytes(byte[] destination, int offsetDestination, byte[] source, int offsetSource, int len){
        if( destination.length < offsetDestination + len ){
            destination = this.realloc(destination, offsetDestination + len);
        }

        System.arraycopy(source, offsetSource, destination, offsetDestination, len);

        return offsetDestination + len;
    }
}

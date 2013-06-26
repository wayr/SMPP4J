package org.wayr.smpp.net;

import java.io.*;
import org.wayr.smpp.utils.MSBNumberToByteConverter;
import org.wayr.smpp.utils.NumberSignificantBitConverter;

/**
 *
 * @author paul
 */
public abstract class Link {

    public static class LinkNotUpException extends IOException {

        public LinkNotUpException() {
        }

        public LinkNotUpException(String string) {
            super(string);
        }

        public LinkNotUpException(Throwable thrwbl) {
            super(thrwbl);
        }

        public LinkNotUpException(String string, Throwable thrwbl) {
            super(string, thrwbl);
        }
    }

    public static class TimeoutUnsupportedException extends IOException {
    }
    protected BufferedInputStream in;
    protected BufferedOutputStream out;
    //--
    protected final Object readLock = new Object();
    protected final Object writeLock = new Object();
    //--
    protected boolean autoFlush;
    protected int inputBufferSize;
    protected int outputBufferSize;
    //--
    NumberSignificantBitConverter numberConverter = new MSBNumberToByteConverter();

    public Link() {
        this(true, -1, -1, new MSBNumberToByteConverter());
    }

    public Link(boolean autoFlush, int inputBufferSize, int outputBufferSize, NumberSignificantBitConverter numberConverter) {
        this.autoFlush = autoFlush;
        this.inputBufferSize = inputBufferSize;
        this.outputBufferSize = outputBufferSize;
        this.numberConverter = numberConverter;
    }

    /**
     * Open the link
     *
     * @throws IOException
     */
    public boolean open() throws IOException {
        this.openLink();

        synchronized (this.readLock) {
            if (this.inputBufferSize < 1) {
                this.in = new BufferedInputStream(this.getInputStream());
            } else {
                this.in = new BufferedInputStream(this.getInputStream(), inputBufferSize);
            }
        }

        synchronized (writeLock) {
            if (this.outputBufferSize < 1) {
                this.out = new BufferedOutputStream(this.getOutputStream());
            } else {
                this.out = new BufferedOutputStream(this.getOutputStream(), outputBufferSize);
            }
        }

        return true;
    }

    public void close() {
        synchronized (this.writeLock) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
                out = null;
            }
        }
        synchronized (this.readLock) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
                in = null;
            }
        }
        try {
            this.closeLink();
        } catch (IOException e) {
        }
    }

    public byte[] read(final byte[] array) throws IOException {
        synchronized (readLock) {
            if (in == null) {
                throw new LinkNotUpException();
            }

            byte[] buf = array;
            int count = this.readBytes(buf, 0, 4, 16);
            int cmdLen = this.numberConverter.toInt(buf, 0, 4);

            if (cmdLen > buf.length) {
                byte[] newbuf = new byte[cmdLen];
                System.arraycopy(buf, 0, newbuf, 0, count);
                buf = newbuf;
            }
            int remaining = cmdLen - count;
            this.readBytes(buf, count, remaining, remaining);

            return buf;
        }
    }

    /**
     * Write packet to the output stream
     *
     * @param packet packet to send
     * @param withOptional include optional ?
     * @throws IOException
     */
    public boolean write(byte[] bytes) throws IOException {
        synchronized (writeLock) {
            if (out == null) {
                throw new LinkNotUpException();
            }

            out.write(bytes);

            if (this.autoFlush) {
                out.flush();
            }

            return true;
        }
    }

    protected int readBytes(byte[] buf, int offset, int minimum, int maxLen) throws IOException {
        synchronized (readLock) {
            int ptr = in.read(buf, offset, maxLen);

            if (ptr < minimum) {
                if (ptr == -1) {
                    throw new EOFException();
                }
                while (ptr < minimum) {
                    int count = in.read(buf, offset + ptr, maxLen - ptr);
                    if (count < 0) {
                        throw new EOFException();
                    }
                    ptr += count;
                }
            }
            return ptr;
        }
    }

    public final int available() throws IOException {
        synchronized (readLock) {
            return (this.in != null) ? this.in.available() : 0;
        }
    }

    /**
     * Flush the output stream
     *
     */
    public void flush() throws IOException {
        synchronized (writeLock) {
            if (out != null) {
                out.flush();
            }
        }
    }

    /**
     * Set the value for read timeout
     *
     * @param timeout the timeout value in milliseconds
     */
    public void setTimeout(int timeout) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /**
     * Get the value for the read timeout
     *
     * @return the current value for read timeout in milliseconds
     */
    public int getTimeout() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /**
     * Implementation-specific to check whether or not the connection is open
     *
     * @return
     */
    public abstract boolean isConnected();

    /**
     * Implementation-specific link open
     *
     * @throws IOException
     */
    protected abstract void openLink() throws IOException;

    /**
     * Implementation-specfic link close
     *
     * @throws IOException
     */
    protected abstract void closeLink() throws IOException;

    /**
     *
     * @return The InputStream
     * @throws IOException
     */
    protected abstract InputStream getInputStream() throws IOException;

    /**
     *
     * @return The OutputStream
     * @throws IOException
     */
    protected abstract OutputStream getOutputStream() throws IOException;
}

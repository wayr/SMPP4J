package org.wayr.smpp.messages.tlv.old;

import org.wayr.smpp.messages.tlv.*;
import org.wayr.smpp.exceptions.InvalidSizeForValueException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.wayr.smpp.GlobalConfiguration;
import org.wayr.smpp.exceptions.BadValueTypeException;
import org.wayr.smpp.utils.BytesSerializable;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class TLVTable implements BytesSerializable {

    protected final Object lock = new Object();
    protected IOStreamConverter converter = GlobalConfiguration.getInstance().getBufferConverter();
    protected Map<Tag, Object> map = new HashMap<Tag, Object>();
    protected byte[] opts;

    public TLVTable() {
    }

    public void readFrom(byte[] b, int offset, int len) {
        synchronized (lock) {
            opts = new byte[len];
            System.arraycopy(b, offset, opts, 0, len);
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        synchronized (lock) {
            byte[] buffer = new byte[1024];

            Iterator i = map.keySet().iterator();
            while (i.hasNext()) {
                Tag t = (Tag) i.next();
                Encoder enc = t.getEncoder();
                Object v = map.get(t);
                int l = enc.getValueLength(t, v);

                if (buffer.length < (l + 4)) {
                    buffer = new byte[l + 4];
                }

                this.converter.writeNumber(buffer, 0, t.intValue(), 2);
                this.converter.writeNumber(buffer, 2, l, 2);
                enc.writeTo(t, v, buffer, 4);

                // write the buffer out.
                out.write(buffer, 0, l + 4);
            }
        }
    }

    public Map<Tag, Object> getMap() {
        return map;
    }

    public Object get(Tag tag) {
        synchronized (lock) {
            Object v = map.get(tag);

            if (v == null) {
                v = getValueFromBytes(tag);
            }
            return v;
        }

    }

    public Object get(int tag) {
        synchronized (lock) {
            Tag tagObj = Tag.getTag(tag);
            Object v = map.get(tagObj);

            if (v == null) {
                v = getValueFromBytes(tagObj);
            }

            return v;
        }
    }

    public boolean isSet(Tag tag) {
        if (opts != null) {
            parseAllOpts();
        }
        return map.containsKey(tag);
    }

    public Object set(Tag tag, Object value) throws BadValueTypeException, InvalidSizeForValueException {
        synchronized (lock) {
            if (opts != null) {
                parseAllOpts();
            }

            if (tag.getType() == null) {
                if (value != null) {
                    throw new BadValueTypeException("Tag " + Integer.toHexString(tag.intValue()) + " does not accept a value.");
                }
            } else if (!tag.getType().isAssignableFrom(value.getClass())) {
                throw new BadValueTypeException("Tag " + Integer.toHexString(tag.intValue()) + " expects a value of type " + tag.getType());
            }

            // Enforce the length restrictions on the Value specified by the
            // Tag.
            int min = tag.getMinLength();
            int max = tag.getMaxLength();
            int actual = tag.getEncoder().getValueLength(tag, value);

            boolean illegal = min > -1 && actual < min;
            if (!illegal) {
                illegal = max > -1 && actual > max;
            }

            if (illegal) {
                throw new InvalidSizeForValueException("Tag " + Integer.toHexString(tag.intValue()) + " must have a length in the range " + min + " <= len <= " + max);
            }

            return map.put(tag, value);
        }
    }

    /**
     * Clear all optional parameters out of this table.
     */
    public void clear() {
        synchronized (lock) {
            map.clear();
        }
    }

    public final void parseAllOpts() {
        synchronized (lock) {
            int p = 0;

            while (p < opts.length) {
                Object val;
                Tag t = Tag.getTag((int)this.converter.readShort(opts, p));
                Encoder enc = t.getEncoder();
                int l = (int) this.converter.readShort(opts, p + 2);

                val = enc.readFrom(t, opts, p + 4, l);
                map.put(t, val);

                p += 4 + l;
            }

            opts = null;
        }
    }

    protected Object getValueFromBytes(Tag tag) {
        if (opts == null || opts.length < 4) {
            return null;
        }

        Encoder enc = tag.getEncoder();
        Object val = null;
        int p = 0;
        while (true) {
            int t = (int) this.converter.readShort(opts, p);
            int l = (int) this.converter.readShort(opts, p + 2);

            if (tag.equals(t)) {
                val = enc.readFrom(tag, opts, p + 4, l);
                synchronized (lock) {
                    map.put(tag, val);
                    break;
                }
            }

            p += 4 + l;
            if (p >= opts.length) {
                break;
            }
        }

        return val;
    }

    public int getLength() {
        if (opts != null) {
            parseAllOpts();
        }

        // Length is going to be (number of options) * (2 bytes for tag) * (2
        // bytes for length) + (size of all encoded values)
        int length = map.size() * 4;
        Tag tag;
        Encoder enc;
        Iterator i = map.keySet().iterator();
        while (i.hasNext()) {
            tag = (Tag) i.next();
            enc = tag.getEncoder();
            length += enc.getValueLength(tag, map.get(tag));
        }

        return length;
    }

    public Set tagSet() {
        if (opts != null) {
            parseAllOpts();
        }

        return map.keySet();
    }

    public Collection values() {
        if (opts != null) {
            parseAllOpts();
        }

        return map.values();
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[0];
        synchronized (lock) {

            int offset = 0;

            Iterator i = map.keySet().iterator();
            while (i.hasNext()) {
                Tag t = (Tag) i.next();
                Encoder enc = t.getEncoder();
                Object v = map.get(t);
                int l = enc.getValueLength(t, v);

                /* if (buffer.length < (l + 4)) {
                    buffer = new byte[l + 4];
                }*/

                offset = this.converter.writeNumber(buffer, offset, t.intValue(), 2);
                offset = this.converter.writeNumber(buffer, offset, l, 2);
                this.converter.realloc(buffer, buffer.length + enc.getValueLength(t, v));
                offset += enc.getValueLength(t, v);

                enc.writeTo(t, v, buffer, 4);
            }
        }
        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] buffer, int offset, int length) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

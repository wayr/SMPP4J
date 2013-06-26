package org.wayr.smpp.messages;

import java.util.*;
import org.wayr.smpp.Address;
import org.wayr.smpp.GlobalConfiguration;
import org.wayr.smpp.utils.BytesSerializable;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class DestinationTable implements BytesSerializable {

    private List<Object> dests;
    private int length = 0;

    DestinationTable() {
        dests = new ArrayList<Object>();
    }

    protected synchronized void add(Address addr) {
        dests.add(addr);
        // Plus 1 for the dest type flag.
        length += addr.getLength() + 1;
    }

    protected synchronized void add(String distList) {
        dests.add(distList);
        // nul byte plus dest type flag
        length += distList.length() + 2;
    }

    public synchronized void remove(Address addr) {
        int i = dests.indexOf(addr);
        if (i > -1) {
            length -= ((Address) dests.remove(i)).getLength() + 1;
        }
    }

    public synchronized void remove(String distList) {
        int i = dests.indexOf(distList);
        if (i > -1) {
            length -= ((String) dests.remove(i)).length() + 2;
        }
    }

    public synchronized Iterator iterator() {
        return Collections.unmodifiableList(dests).iterator();
    }

    public synchronized ListIterator listIterator() {
        return Collections.unmodifiableList(dests).listIterator();
    }

    public synchronized int getLength() {
        return length;
    }

    public synchronized int size() {
        return dests.size();
    }

    @Override
    public synchronized byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[0];
        int offset = 0;

        Iterator i = dests.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Address) {

                offset = converter.writeByte(buffer, offset, 1);
                offset = converter.writeBytes(buffer, offset, ((Address) o).toBytes(converter));
            } else {
                offset = converter.writeByte(buffer, offset, 2);
                offset = converter.writeCString(buffer, offset, (String) o);
            }
        }

        return buffer;
    }

    @Override
    public synchronized int fromBytes(IOStreamConverter converter, byte[] table, int offset, int count) {
        length = 0;
        int startOffset = offset;

        for (int i = 0; i < count; i++) {
            int type = (int) converter.readByte(table, offset++);

            if (type == 1) {
                // SME address..
                Address a = new Address();
                a.fromBytes(converter, table, offset, -1);
                offset += a.getLength();
                dests.add(a);
            } else if (type == 2) {
                // Distribution list name
                String d = converter.readCString(table, offset);
                offset += d.length() + 1;
                dests.add(d);
            } else {
                GlobalConfiguration.getInstance().getLogger().warn("Unidentified destination type on input.");
            }
        }

        this.length = offset - startOffset;

        return offset;
    }

    @Override
    public synchronized Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException x) {
            throw new RuntimeException("Clone not supported", x);
        }
    }
}

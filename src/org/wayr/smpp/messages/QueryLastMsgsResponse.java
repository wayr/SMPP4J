package org.wayr.smpp.messages;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.wayr.smpp.exceptions.InvalidParameterValueException;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class QueryLastMsgsResponse extends SMPPResponse {

    private List<String> messageTable = new LinkedList<String>();

    public QueryLastMsgsResponse() {
        super(QUERY_LAST_MSGS_RESP);
    }

    public synchronized int addMessageId(String id) {
        if (!version.validateMessageId(id)) {
            throw new InvalidParameterValueException("Invalid message ID", id);
        }

        messageTable.add(id);
        return messageTable.size();
    }

    public synchronized int getMsgCount() {
        return messageTable.size();
    }

    public synchronized String[] getMessageIds() {
        String[] ids;
        int loop = 0;

        if (messageTable.isEmpty()) {
            return null;
        }

        ids = new String[messageTable.size()];
        Iterator i = messageTable.iterator();
        while (i.hasNext()) {
            ids[loop++] = (String) i.next();
        }

        return ids;
    }

    @Override
    public synchronized int getBodyLength() {
        int size = 1;
        Iterator i = messageTable.iterator();
        while (i.hasNext()) {
            size += ((String) i.next()).length() + 1;
        }

        return size;
    }

    @Override
    public synchronized byte[] toBytes(IOStreamConverter converter) {
        byte[] buffer = new byte[this.getBodyLength()];
        int offset = 0;

        int size = messageTable.size();
        converter.writeByte(buffer, offset, size);
        Iterator<String> i = messageTable.iterator();
        while (i.hasNext()) {
            offset = converter.writeCString(buffer, offset, i.next());
        }

        return buffer;
    }

    @Override
    public synchronized int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        int msgCount = (int)converter.readByte(body, offset++);

        for (int loop = 0; loop < msgCount; loop++) {
            String s = converter.readCString(body, offset);
            offset += s.length() + 1;
            messageTable.add(s);
        }

        return offset;
    }
}

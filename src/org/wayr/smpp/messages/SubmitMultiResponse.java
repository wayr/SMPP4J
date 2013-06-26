package org.wayr.smpp.messages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.wayr.smpp.Address;
import org.wayr.smpp.ErrorAddress;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class SubmitMultiResponse extends SMPPResponse {

    protected List<ErrorAddress> unsuccessfullTable = Collections.synchronizedList(new ArrayList<ErrorAddress>());

    public SubmitMultiResponse() {
        super(SUBMIT_MULTI_RESP);
    }

    public SubmitMultiResponse(SubmitMultiRequest q) {
        super(q);
    }

    public synchronized List<ErrorAddress> getUnsuccessfullTable()
    {
        return this.unsuccessfullTable;
    }

    public synchronized int add(ErrorAddress ea) {
        unsuccessfullTable.add(ea);
        return unsuccessfullTable.size();
    }

    public synchronized int remove(Address a) {
        int i = unsuccessfullTable.indexOf(a);
        if (i > -1) {
            unsuccessfullTable.remove(i);
        }

        return unsuccessfullTable.size();
    }

    @Override
    public synchronized int getBodyLength() {
        int size = (messageId != null) ? messageId.length() : 0;

        Iterator i = unsuccessfullTable.iterator();
        while (i.hasNext()) {
            size += ((ErrorAddress) i.next()).getLength();
        }

        // 1 1-byte integer, 1 c-string
        return size + 1 + 1;
    }

    @Override
    public synchronized byte[] toBytes(IOStreamConverter converter) {
        int size;

        byte[] buffer = new byte[this.getBodyLength()];
        int offset = 0;

        size = unsuccessfullTable.size();
        offset = converter.writeCString(buffer, offset, getMessageId());
        converter.writeByte(buffer, offset++, size);

        Iterator<ErrorAddress> i = unsuccessfullTable.iterator();
        while (i.hasNext()) {

            offset = converter.writeBytes(buffer, offset, i.next().toBytes(converter));
        }
        return buffer;
    }

    @Override
    public synchronized int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {

        messageId = converter.readCString(body, offset);
        offset += messageId.length() + 1;

        int unsuccessfulCount = (int)converter.readByte(body, offset++);

        if (unsuccessfulCount < 1) {
            return offset;
        }

        for (int loop = 0; loop < unsuccessfulCount; loop++) {
            ErrorAddress a = new ErrorAddress();
            offset = a.fromBytes(converter, body, offset, -1);
            unsuccessfullTable.add(a);
        }

        return offset;
    }
}

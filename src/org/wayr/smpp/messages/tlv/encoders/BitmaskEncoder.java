package org.wayr.smpp.messages.tlv.encoders;

import java.util.BitSet;
import org.wayr.smpp.exceptions.BadValueTypeException;
import org.wayr.smpp.messages.tlv.old.Encoder;
import org.wayr.smpp.messages.tlv.old.Tag;

/**
 *
 * @author paul
 */
public class BitmaskEncoder implements Encoder{

    public BitmaskEncoder() {
    }

    @Override
    public void writeTo(Tag tag, Object value, byte[] b, int offset) {
        try {
            BitSet bs = (BitSet) value;
            int l = tag.getLength();

            for (int i = 0; i < l; i++) {
                b[offset + i] = 0;

                for (int j = 0; j < 8; j++) {
                    if (bs.get((i * 8) + j)) {
                        b[offset + i] |= (byte) (1 << j);
                    }
               }
            }
        } catch (ClassCastException x) {
            throw new BadValueTypeException("Value must be of type java.util.BitSet");
        }
    }

    @Override
    public Object readFrom(Tag tag, byte[] b, int offset, int length) {
        BitSet bs = new BitSet();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < 8; j++) {
                if ((b[offset + i] & (byte) (1 << j)) != 0) {
                    bs.set((i * 8) + j);
                }
            }
        }

        return bs;
    }

    @Override
    public int getValueLength(Tag tag, Object value) {
        return tag.getLength();
    }

}

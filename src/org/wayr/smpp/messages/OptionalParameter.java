package org.wayr.smpp.messages;

import org.wayr.smpp.messages.tlv.Tag;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.wayr.smpp.utils.BytesSerializable;
import org.wayr.smpp.utils.BytesUtils;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public abstract class OptionalParameter<E> implements BytesSerializable {

    public final short tag;

    public OptionalParameter(Short tag) {
        this.tag = tag;
    }

    protected abstract byte[] serializeValue(IOStreamConverter converter);

    public abstract void setValue(E value);

    public abstract E getValue();

    @Override
    public byte[] toBytes(IOStreamConverter converter) {
        byte[] value = serializeValue(converter);
        ByteBuffer buffer = ByteBuffer.allocate(4 + value.length);
        buffer.putShort(tag);
        buffer.putShort((short) value.length);
        buffer.put(value);
        return buffer.array();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tag;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        OptionalParameter other = (OptionalParameter) obj;
        if (tag != other.tag) {
            return false;
        }
        return true;
    }

    public static class NullParameter extends OptionalParameter<Object> {

        public NullParameter(Short tag) {
            super(tag);
        }

        public NullParameter(Tag tag) {
            this(tag.code());
        }

        @Override
        protected byte[] serializeValue(IOStreamConverter converter) {
            return new byte[0];
        }

        @Override
        public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
            return offset;
        }

        @Override
        public void setValue(Object value) {
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public String toString() {
            return String.format("%04X 0000", tag);
        }
    }

    public static class ByteParameter extends OptionalParameter<Byte> {

        protected Byte value;

        public ByteParameter(Short tag) {
            super(tag);
        }

        public ByteParameter(Short tag, Byte value) {
            super(tag);
            this.value = value;
        }

        public ByteParameter(Tag tag, Byte value) {
            this(tag.code(), value);
        }

        @Override
        public Byte getValue() {
            return this.value;
        }

        @Override
        public void setValue(Byte value) {
            this.value = value;
        }

        @Override
        protected byte[] serializeValue(IOStreamConverter converter) {
            byte[] buffer = new byte[1];
            converter.writeByte(buffer, 0, value);
            return buffer;
        }

        @Override
        public String toString() {
            return String.format("%04X 0001 %01X", tag, value);
        }

        @Override
        public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
            value = converter.readByte(body, offset++);
            return offset;
        }
    }

    public static class ShortParameter extends OptionalParameter<Short> {

        protected Short value;

        public ShortParameter(Short tag) {
            super(tag);
        }

        public ShortParameter(Short tag, Short value) {
            super(tag);
            this.value = value;
        }

        public ShortParameter(Tag tag, Short value) {
            this(tag.code(), value);
        }

        @Override
        public Short getValue() {
            return value;
        }

        @Override
        public void setValue(Short value) {
            this.value = value;
        }

        @Override
        protected byte[] serializeValue(IOStreamConverter converter) {
            byte[] buffer = new byte[2];
            converter.writeShort(buffer, 0, value);
            return buffer;
        }

        @Override
        public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
            value = converter.readShort(body, offset);
            return offset + 2;
        }

        @Override
        public String toString() {
            return String.format("%04X 0002 %02X", tag, value);
        }
    }

    public static class IntegerParameter extends OptionalParameter<Integer> {

        protected int value;

        public IntegerParameter(Short tag) {
            super(tag);
        }

        public IntegerParameter(Short tag, Integer value) {
            super(tag);
            this.value = value;
        }

        public IntegerParameter(Tag tag, Integer value) {
            this(tag.code(), value);
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        protected byte[] serializeValue(IOStreamConverter converter) {
            byte[] buffer = new byte[4];
            converter.writeInt(buffer, 0, value);
            return buffer;
        }

        @Override
        public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
            value = converter.readInt(body, offset);
            return offset + 4;
        }

        @Override
        public String toString() {
            return String.format("%04X 0004 %04X", tag, value);
        }
    }

    public static class LongParameter extends OptionalParameter<Long> {

        protected long value;

        public LongParameter(Short tag) {
            super(tag);
        }

        public LongParameter(Short tag, Long value) {
            super(tag);
            this.value = value;
        }

        public LongParameter(Tag tag, Long value) {
            this(tag.code(), value);
        }

        @Override
        public Long getValue() {
            return value;
        }

        @Override
        public void setValue(Long value) {
            this.value = value;
        }

        @Override
        protected byte[] serializeValue(IOStreamConverter converter) {
            byte[] buffer = new byte[8];
            converter.writeLong(buffer, 0, value);
            return buffer;
        }

        @Override
        public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
            value = converter.readLong(body, offset);
            return offset + 8;
        }

        @Override
        public String toString() {
            return String.format("%04X 0008 %08X", tag, value);
        }
    }

    public static class OctetStringParameter extends OptionalParameter<Byte[]> {

        protected byte[] value;

        public OctetStringParameter(Short tag) {
            super(tag);
        }

        public OctetStringParameter(Short tag, String value) {
            super(tag);
            this.value = value.getBytes();
        }

        public OctetStringParameter(Tag tag, String value) {
            this(tag.code(), value);
        }

        public OctetStringParameter(short tag, String value, String charsetName) throws UnsupportedEncodingException {
            super(tag);
            this.value = value.getBytes(charsetName);
        }

        public OctetStringParameter(short tag, byte[] value, int offset, int length) {
            super(tag);
            this.value = new byte[length];
            System.arraycopy(value, offset, this.value, offset, length);
        }

        @Override
        public Byte[] getValue() {
            Byte[] bytes = new Byte[this.value.length];
            for (int i = 0; i < this.value.length; i++) {
                bytes[i] = this.value[i];
            }
            return bytes;
        }

        @Override
        public void setValue(Byte[] value) {
            this.value = new byte[value.length];
            for (int i = 0; i < value.length; i++) {
                this.value[i] = value[i];
            }
        }

        public byte[] getBytesValue() {
            return this.value;
        }

        public String getValueAsString() {
            return new String(value);
        }

        @Override
        protected byte[] serializeValue(IOStreamConverter converter) {
            byte[] buffer = new byte[1];
            converter.writeBytes(buffer, 0, value);
            return buffer;
        }

        @Override
        public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
            value = converter.readBytes(body, offset, length);
            return offset + 8;
        }

        @Override
        public String toString() {
            return String.format("%04X %04X %s", tag, value == null ? 0 : value.length, BytesUtils.bytesToHexString(value));
        }
    }

    public static class COctetStringParameter extends OctetStringParameter {

        public COctetStringParameter(Short tag) {
            super(tag);
        }

        public COctetStringParameter(Short tag, String value, String charsetName) throws UnsupportedEncodingException {
            super(tag, value, charsetName);
        }

        public COctetStringParameter(Short tag, String value) {
            super(tag, value);
        }

        public COctetStringParameter(Tag tag, String value) {
            super(tag, value);
        }

        @Override
        public String getValueAsString() {
            return new String(getBytesValue(), 0, value.length - 1);
        }
    }
}

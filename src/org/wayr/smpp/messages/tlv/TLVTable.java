package org.wayr.smpp.messages.tlv;

import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.utils.BytesSerializable;
import org.wayr.smpp.utils.IOStreamConverter;

/**
 *
 * @author paul
 */
public class TLVTable implements BytesSerializable {

    private static final Logger logger = LoggerFactory.getLogger(TLVTable.class);
    protected Map<Tag, OptionalParameter> map;

    public TLVTable() {
        this.map = new EnumMap<Tag, OptionalParameter>(Tag.class);
    }

    public Map<Tag, OptionalParameter> getMap() {
        return map;
    }

    public void setMap(Map<Tag, OptionalParameter> map) {
        this.map = map;
    }

    public OptionalParameter get(Tag tag) {
        return map.get(tag);
    }

    public <E extends OptionalParameter> E get(Class<E> tagClass) {
        for( Tag tag: map.keySet() ) {
            if( map.get(tag).getClass() == tagClass ) {
                return (E) map.get(tag);
            }
        }
        return null;
    }

    public OptionalParameter get(int tag) {
        return this.get(Tag.valueOf((short) tag));
    }

    public boolean isSet(Tag tag) {
        return this.map.containsKey(tag);
    }

    public void set(OptionalParameter parameter) {
        this.set(Tag.valueOf(parameter.tag), parameter);
    }

    public void set(Tag tag, OptionalParameter parameter) {
        this.map.put(tag, parameter);
    }

    public void clear() {
        this.map.clear();
    }

    @Override
    public byte[] toBytes(IOStreamConverter converter) {

        byte[] buffer = new byte[0];
        int offset = 0;
        for( Tag tag : this.map.keySet() ) {
            OptionalParameter parameter = this.map.get(tag);
            byte[] value = parameter.toBytes(converter);
            if( buffer.length < offset + value.length ) {
                buffer = converter.realloc(buffer, offset + value.length);
            }
            offset = converter.writeBytes(buffer, offset, value);
        }

        return buffer;
    }

    @Override
    public int fromBytes(IOStreamConverter converter, byte[] body, int offset, int length) {
        while (offset < length) {
            short tagCode = converter.readShort(body, offset);
            offset += 2;
            short valueLength = converter.readShort(body, offset);
            offset += 2;
            byte[] value = converter.readBytes(body, offset, valueLength);
            offset += valueLength;

            OptionalParameter parameter = this.deserialize(tagCode);
            parameter.fromBytes(converter, value, 0, valueLength);
            this.set(parameter);
        }

        return offset;
    }

    protected OptionalParameter deserialize(short tagCode) {
        Tag tag = Tag.valueOf(tagCode);
        if( tag == null ) {
            logger.error("tag {} doesnt exists", tagCode);
            return new OptionalParameter.COctetStringParameter(tagCode);
        }
        return tag.newInstance();
    }

    @Override
    public String toString() {
        return "" + this.map;
    }
}

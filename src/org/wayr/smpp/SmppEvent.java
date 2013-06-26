package org.wayr.smpp;

import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author paul
 */
public class SmppEvent {

    public enum Type {

        LINK_UP(0x00, "link.up"),
        SMPP_READ(0x01, "smpp.read"),
        SMPP_WRITE(0x02, "smpp.write"),
        SMPP_BIND(0x03, "smpp.bind"),
        SMPP_STATE(0x04, "smpp.state")
        ;

        private final int value;
        private final String name;
        private static final Map<Integer, Type> lookup = new HashMap<Integer, Type>();
        private static final Map<String, Type> slookup = new HashMap<String, Type>();

        static {
            for (Type s : EnumSet.allOf(Type.class)) {
                lookup.put(s.getValue(), s);
                slookup.put(s.getName(), s);
            }
        }

        private Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public int getValue() {
            return this.value;
        }

        public static Type get(int code) throws IllegalArgumentException {
            if (!lookup.containsKey(code)) {
                throw new IllegalArgumentException("No connection state found for code:" + code);
            }
            return lookup.get(code);
        }

        public static Type get(String name) throws IllegalArgumentException {
            if (!slookup.containsKey(name)) {
                throw new IllegalArgumentException("No connection state found for name:" + name);
            }
            return slookup.get(name);
        }

        @Override
        public String toString() {
            return "Smpp.Type(" + this.value + ", '" + this.name() + "')";
        }


    }
    protected Type type = null;
    protected Date createdAt = new Date();
    protected Object content = null;

    public SmppEvent(Type type, Object content) {
        this.type = type;
        this.content = content;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type + ":\n" + this.content;
    }
}

package org.wayr.smpp.connection;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author paul
 */
public enum ConnectionType {

    TRANSMITTER(0),
    RECEIVER(1),
    TRANSCEIVER(2);
    private final int value;
    private static final Map<Integer, ConnectionType> lookup = new HashMap<Integer, ConnectionType>();

    static {
        for (ConnectionType s : EnumSet.allOf(ConnectionType.class)) {
            lookup.put(s.getValue(), s);
        }
    }

    private ConnectionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ConnectionType get(int code) throws IllegalArgumentException {
        if (!lookup.containsKey(code)) {
            throw new IllegalArgumentException("No connection type found for code:" + code);
        }
        return lookup.get(code);
    }
}

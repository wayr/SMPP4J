package org.wayr.smpp.connection;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author paul
 */
public enum ConnectionState {

    UNKNOWN(-1),
    OPEN(1),
    BINDING(2),
    BOUND(3),
    UNBINDING(4),
    UNBOUND(5),
    CLOSED(6);
    private final int value;
    private static final Map<Integer, ConnectionState> lookup = new HashMap<Integer, ConnectionState>();

    static {
        for (ConnectionState s : EnumSet.allOf(ConnectionState.class)) {
            lookup.put(s.getValue(), s);
        }
    }

    private ConnectionState(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static ConnectionState get(int code) throws IllegalArgumentException {
        if (!lookup.containsKey(code)) {
            throw new IllegalArgumentException("No connection state found for code:" + code);
        }
        return lookup.get(code);
    }
}

package org.wayr.smpp.listeners;

import org.wayr.smpp.connection.ConnectionState;
import org.wayr.smpp.connection.SmppConnectionClient;

/**
 *
 * @author paul
 */
public interface ConnectionClientStateListener {

    public void onStateChange(ConnectionState newState, ConnectionState oldState, final SmppConnectionClient connection);

}

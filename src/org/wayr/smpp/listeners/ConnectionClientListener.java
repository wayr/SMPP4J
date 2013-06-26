package org.wayr.smpp.listeners;

import java.io.IOException;
import org.wayr.smpp.Packet;
import org.wayr.smpp.connection.SmppConnectionClient;

/**
 *
 * @author paul
 */
public interface ConnectionClientListener {

    public Packet beforeWrite(Packet packet, SmppConnectionClient destination) throws IOException;

    public void afterWrite(Packet packet, SmppConnectionClient destination, boolean success);

    public void onRead(Packet packet, SmppConnectionClient source);
}

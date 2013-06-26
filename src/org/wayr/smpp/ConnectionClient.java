package org.wayr.smpp;

import java.io.IOException;
import org.wayr.smpp.connection.ConnectionState;
import org.wayr.smpp.exceptions.BadCommandIDException;
import org.wayr.smpp.exceptions.NotBoundException;
import org.wayr.smpp.messages.BindRequest;

/**
 *
 * @author paul
 */
public interface ConnectionClient extends Connection {

    public ConnectionState getState();

    public void setState(ConnectionState state);

    public BindRequest bind() throws BadCommandIDException, IOException;

    public boolean unbind(boolean force) throws IOException, NotBoundException, BadCommandIDException;

}

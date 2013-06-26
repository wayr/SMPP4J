package org.wayr.smpp;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.wayr.smpp.net.Link;

/**
 *
 * @author paul
 */
public interface Connection {

    public int getConnectionTimeout();

    public void setConnectionTimeout(int connectionTimeout);

    public boolean open() throws Link.LinkNotUpException;

    public boolean isOpen();

    public void close();

    public Packet read() throws IOException, InterruptedException;

    public Packet read(long l, TimeUnit tu) throws IOException, InterruptedException;

    public boolean write(Packet packet) throws IOException;
}

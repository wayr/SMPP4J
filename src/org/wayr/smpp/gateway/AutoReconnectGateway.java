package org.wayr.smpp.gateway;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wayr.smpp.GlobalConfiguration;
import org.wayr.smpp.Packet;
import org.wayr.smpp.connection.ConnectionState;
import static org.wayr.smpp.connection.ConnectionState.BINDING;
import static org.wayr.smpp.connection.ConnectionState.BOUND;
import static org.wayr.smpp.connection.ConnectionState.UNBINDING;
import org.wayr.smpp.connection.AbstractSmppConnection;
import org.wayr.smpp.connection.SmppConnectionClient;
import org.wayr.smpp.exceptions.BadCommandIDException;
import org.wayr.smpp.exceptions.NotBoundException;
import org.wayr.smpp.exceptions.VersionException;
import org.wayr.smpp.listeners.ConnectionClientStateListener;
import org.wayr.smpp.listeners.ConnectionClientListener;
import org.wayr.smpp.messages.EnquireLinkRequest;
import org.wayr.smpp.net.Link;

/**
 *
 * @author paul
 */
public class AutoReconnectGateway extends Thread implements ConnectionClientStateListener {

    protected static Logger logger = LoggerFactory.getLogger(AutoReconnectGateway.class);
    //--
    protected boolean shouldStop = false;
    //--
    protected SmppConnectionClient connection;
    //--
    protected ExecutorService reconnectProcessor = Executors.newFixedThreadPool(1);
    //--
    protected long lastPacketAt = 0;
    //--
    protected ConnectionClientStateListener connectionStateListener;
    //--
    private final Object connectionOpenLock = new Object();

    public AutoReconnectGateway() {
        super("smpp-gateway");
    }

    public void setConnection(SmppConnectionClient connection) {
        this.connection = connection;
        this.connection.setConnectionStateListener(this);
    }

    public AbstractSmppConnection getConnection() {
        return connection;
    }

    public ConnectionClientStateListener getConnectionStateListener() {
        return connectionStateListener;
    }

    public void setConnectionStateListener(ConnectionClientStateListener connectionStateListener) {
        this.connectionStateListener = connectionStateListener;
    }

    public void setIOListener(ConnectionClientListener ioListener) {
        this.connection.setListener(ioListener);
    }

    public ConnectionClientListener getIOListener() {
        return this.connection.getListener();
    }

    public boolean open() throws Link.LinkNotUpException {
        return this.connection.open();
    }

    public void close() {
        if( !this.isRunning() ) {
            return;
        }
        logger.trace("closing autoreconnect");
        this.shouldStop();
        this.reconnectProcessor.shutdown();
        try {
            logger.trace("unbinding autoreconnect");
            this.connection.unbind(false);
        } catch (NotBoundException e) {
            logger.trace(null, e);
        } catch (BadCommandIDException e) {
        } catch (IOException e) {
            logger.trace(null, e);
        }

        this.connection.close();
    }

    public synchronized boolean isRunning() {
        return !this.shouldStop;
    }

    public synchronized void shouldStop() {
        this.shouldStop = true;
    }

    @Override
    public synchronized void onStateChange(ConnectionState newState, ConnectionState oldState, final SmppConnectionClient source) {
        switch (newState) {
            case UNBOUND:
                if (this.isRunning()) {
                    reconnectProcessor.execute(new ConnectionCloseTask(source));
                }
                break;
            case CLOSED:
                break;
            case OPEN:
                if (this.isRunning()) {
                    reconnectProcessor.execute(new ConnectionBindTask(source));
                }
                break;
            case BINDING:
            case UNBINDING:
                break;
            case BOUND:
                break;
        }

        if (null != this.getConnectionStateListener()) {
            this.getConnectionStateListener().onStateChange(newState, oldState, source);
        }
    }

    public boolean write(Packet packet) throws IOException {
        return this.connection.write(packet);
    }

    protected boolean isConnectionClosed() throws InterruptedException {
        try {
            if (!this.connection.isOpen()) {
                return !this.open();
            }
            return false;
        } catch (Link.LinkNotUpException e) {
            GlobalConfiguration.getInstance().getLogger().error("link {} is not up: {}", this.connection.getSmscAddress(), e.getCause().getMessage());
            synchronized (this.connectionOpenLock) {
                this.connectionOpenLock.wait(3000l);
            }
            return true;
        }
    }

    protected void onStart() {
        this.lastPacketAt = System.currentTimeMillis();
    }

    protected void onClose() {
        this.close();
    }

    @Override
    public void run() {
        this.onStart();
        try {
            while (!Thread.currentThread().isInterrupted() && this.isRunning()) {
                if( this.isConnectionClosed() ) {
                    continue;
                }

                try {
                    Packet elt = this.connection.read();
                    if (elt == null) {
                        throw new SocketTimeoutException("no packet found.");
                    }
                    this.pingPacket();

                } catch (IOException e) {
                    this.onIOException(e);
                } catch (Exception e) {
                    GlobalConfiguration.getInstance().getLogger().error(null, e);
                }
            }
        } catch (InterruptedException e) {
            logger.trace("", e);
            Thread.currentThread().interrupt();
        }

        this.onClose();
    }

    private void pingPacket() {
        this.lastPacketAt = System.currentTimeMillis();
    }

    private void onIOException(IOException e) {
        if (e instanceof SocketTimeoutException) {
            long ellapse = System.currentTimeMillis() - this.lastPacketAt;

            if (ellapse > connection.getConnectionTimeout() * 2) {
                connection.setState(ConnectionState.CLOSED);
                logger.trace("{}ms without a packet", ellapse);
            } else if (connection.getState() == ConnectionState.BOUND) {
                try {
                    EnquireLinkRequest ping = (EnquireLinkRequest) connection.makePacket(Packet.ENQUIRE_LINK);
                    connection.write(ping);
                } catch (BadCommandIDException ex) {
                } catch (VersionException ex) {
                } catch (IOException ex) {
                    connection.close();
                }
            }
        } else {
            connection.close();
        }
    }

    private abstract static class ConnectionStateTask implements Runnable {

        protected AbstractSmppConnection source;

        public ConnectionStateTask(AbstractSmppConnection source) {
            this.source = source;
        }
    }

    private static class ConnectionCloseTask extends ConnectionStateTask {

        public ConnectionCloseTask(AbstractSmppConnection source) {
            super(source);
        }

        @Override
        public void run() {
            source.close();
        }
    }

    private static class ConnectionBindTask extends ConnectionStateTask {

        public ConnectionBindTask(AbstractSmppConnection source) {
            super(source);
        }

        @Override
        public void run() {
            try {
                ((SmppConnectionClient) source).bind();
            } catch (BadCommandIDException e) {
                logger.error("SMSC doesnt support BIND ?", e);
            } catch (ClassCastException e) {
                logger.error("{}", e);
            } catch (IOException e) {
                logger.error("{}", e);
            }
        }
    }
}

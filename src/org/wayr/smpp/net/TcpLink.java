package org.wayr.smpp.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

/**
 *
 * @author paul
 */
public class TcpLink extends Link {

    protected static final String SOCKET_NOT_OPEN_EXCEP = "Socket connection is not open";
    protected int port;
    protected InetAddress address;
    protected Socket socket;
    protected int socketTimeout;
    protected boolean connected;

    public TcpLink(String address, int port) throws UnknownHostException {
        super();
        this.address = InetAddress.getByName(address);
        this.port = port;
    }

    public TcpLink(InetAddress address, int port) {
        super();
        this.address = address;
        this.port = port;
    }

    @Override
    public synchronized int getTimeout() {
        try {
            if (this.socket == null) {
                throw new IOException(SOCKET_NOT_OPEN_EXCEP);
            }

            return this.socket.getSoTimeout();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized InetAddress getAddress() {
        return address;
    }

    public synchronized void setAddress(InetAddress address) {
        this.address = address;
    }

    public synchronized int getPort() {
        return port;
    }

    public synchronized void setPort(int port) {
        this.port = port;
    }

    public synchronized Socket getSocket() {
        return socket;
    }

    @Override
    public synchronized void setTimeout(int socketTimeout) {
        try {
            this.socketTimeout = socketTimeout;
            if (this.socket != null) {
                this.socket.setSoTimeout(socketTimeout);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized boolean isConnected() {
        return this.connected;
    }

    @Override
    protected synchronized void openLink() throws IOException {
        this.socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(this.address, this.port);
        this.socket.connect(socketAddress, this.socketTimeout);
        if (this.socketTimeout > 0) {
            this.socket.setSoTimeout(this.socketTimeout);
        }
        this.connected = true;
    }

    @Override
    protected synchronized void closeLink() throws IOException {
        if (this.connected && this.socket != null) {
            try {
                this.socket.close();
                this.socket = null;
                this.connected = false;
            } catch (IOException e) {
                this.connected = false;
                throw e;
            }
        }
    }

    @Override
    protected synchronized InputStream getInputStream() throws IOException {
        if (this.socket == null) {
            throw new IOException(SOCKET_NOT_OPEN_EXCEP);
        }

        return this.socket.getInputStream();
    }

    @Override
    protected synchronized OutputStream getOutputStream() throws IOException {
        if (this.socket == null) {
            throw new IOException(SOCKET_NOT_OPEN_EXCEP);
        }

        return this.socket.getOutputStream();
    }

    public synchronized int getLocalPort() throws IOException {
        if (this.socket == null) {
            throw new IOException(SOCKET_NOT_OPEN_EXCEP);
        }

        return this.socket.getLocalPort();
    }

    public synchronized int getConnectedPort() throws IOException {
        if (this.socket == null) {
            throw new IOException(SOCKET_NOT_OPEN_EXCEP);
        }

        return this.socket.getPort();
    }
}

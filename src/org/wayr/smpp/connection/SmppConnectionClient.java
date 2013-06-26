package org.wayr.smpp.connection;

import org.wayr.smpp.listeners.ConnectionClientInPacketListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import org.wayr.smpp.ConnectionClient;
import org.wayr.smpp.GlobalConfiguration;
import org.wayr.smpp.Packet;
import org.wayr.smpp.SmppResponseCode;
import static org.wayr.smpp.connection.ConnectionType.RECEIVER;
import static org.wayr.smpp.connection.ConnectionType.TRANSCEIVER;
import static org.wayr.smpp.connection.ConnectionType.TRANSMITTER;
import org.wayr.smpp.exceptions.BadCommandIDException;
import org.wayr.smpp.exceptions.NotBoundException;
import org.wayr.smpp.exceptions.VersionException;
import org.wayr.smpp.listeners.ConnectionClientListener;
import org.wayr.smpp.listeners.ConnectionClientStateListener;
import org.wayr.smpp.messages.BindRequest;
import org.wayr.smpp.messages.BindResponse;
import org.wayr.smpp.messages.CancelSMResponse;
import org.wayr.smpp.messages.DeliverSMRequest;
import org.wayr.smpp.messages.DeliverSMResponse;
import org.wayr.smpp.messages.EnquireLinkRequest;
import org.wayr.smpp.messages.EnquireLinkResponse;
import org.wayr.smpp.messages.GenericNackResponse;
import org.wayr.smpp.messages.ParamRetrieveResponse;
import org.wayr.smpp.messages.QueryLastMsgsResponse;
import org.wayr.smpp.messages.QueryMsgDetailsResponse;
import org.wayr.smpp.messages.QuerySMResponse;
import org.wayr.smpp.messages.ReplaceSMResponse;
import org.wayr.smpp.messages.SMPPRequest;
import org.wayr.smpp.messages.SubmitMultiResponse;
import org.wayr.smpp.messages.SubmitSMResponse;
import org.wayr.smpp.messages.UnbindRequest;
import org.wayr.smpp.messages.UnbindResponse;
import org.wayr.smpp.messages.parameters.ScInterfaceVersion;
import org.wayr.smpp.net.TcpLink;
import org.wayr.smpp.version.SMPPVersion;

/**
 *
 * @author paul
 */
public class SmppConnectionClient extends AbstractSmppConnection implements ConnectionClient, ISmppConnection {

    protected ConnectionClientInPacketListener inPacketListener;
    protected ConnectionClientListener listener;
    //--
    protected ConnectionClientStateListener connectionStateListener;
    protected String smscAddress;

    public SmppConnectionClient() {
        super();
    }

    public ConnectionClientInPacketListener getInPacketListener() {
        return inPacketListener;
    }

    public void setInPacketListener(ConnectionClientInPacketListener inPacketListener) {
        this.inPacketListener = inPacketListener;
    }

    @Override
    public void setListener(ConnectionClientListener ioListener) {
        this.listener = ioListener;
    }

    @Override
    public ConnectionClientListener getListener() {
        return this.listener;
    }

    public ConnectionClientStateListener getConnectionStateListener() {
        return connectionStateListener;
    }

    public void setConnectionStateListener(ConnectionClientStateListener connectionStateListener) {
        this.connectionStateListener = connectionStateListener;
    }

    @Override
    public synchronized void setState(ConnectionState state) {
        if( this.state != state) {
            ConnectionState oldState = this.state;
            GlobalConfiguration.getInstance().getLogger().trace("changing state from {} to {}", oldState, state, new Exception());
            super.setState(state);
            if (null != this.connectionStateListener) {
                this.connectionStateListener.onStateChange(state, oldState, this);
            }
        } else {
            GlobalConfiguration.getInstance().getLogger().trace("duplicate state {} == {}", this.state, state, new Exception());
        }
    }

    public String getSmscAddress() {
        return smscAddress;
    }

    public void setSmscAddress(String smscAddress) {
        this.smscAddress = smscAddress;
    }

    public void setAddress(String address) throws UnknownHostException {
        this.smscAddress = address;
        String[] cfg = address.split(":");

        String host = "127.0.0.1";
        Integer port = 9999;

        if (cfg.length == 2) {
            host = cfg[0];
            String sPort = cfg[1];
            port = Integer.parseInt(sPort);
        }

        this.setLink(new TcpLink(host, port));
    }

    @Override
    public Packet read() throws IOException {
        Packet packet = super.read();
        // GlobalConfiguration.getInstance().getLogger().trace(packet.getClass().getSimpleName() + ":" + packet);
        this.onRead(packet);
        return packet;
    }

    @Override
    public Packet read(long l, TimeUnit tu) throws IOException, InterruptedException {
        Packet packet = super.read(l, tu);
        this.onRead(packet);
        return packet;
    }

    @Override
    public boolean write(Packet packet) throws IOException {
        packet = this.beforeWrite(packet);
        boolean success = super.write(packet);
        this.afterWrite(packet, success);
        return success;
    }

    @Override
    public synchronized BindRequest bind() throws BadCommandIDException, IOException {
        if (this.getState() != ConnectionState.OPEN) {
            throw new IllegalStateException("Cannot bind while in state " + this.getState());
        }

        if (!this.isOpen() && !this.open()) {
            throw new UnsupportedOperationException("Cant open link");
        }

        if (this.sequenceNumber != null) {
            this.sequenceNumber.reset();
        }

        BindRequest bindReq = null;
        switch (type) {
            case TRANSMITTER:
                bindReq = (BindRequest) this.packetFactory.make(Packet.BIND_TRANSMITTER, this);
                break;
            case RECEIVER:
                bindReq = (BindRequest) this.packetFactory.make(Packet.BIND_RECEIVER, this);
                break;
            case TRANSCEIVER:
                bindReq = (BindRequest) this.packetFactory.make(Packet.BIND_TRANSCEIVER, this);
                break;
            default:
                throw new IllegalArgumentException("No such connection type:" + type);
        }

        bindReq.setSystemId(systemId);
        bindReq.setPassword(password);
        bindReq.setSystemType(systemType);
        bindReq.setAddressTon(typeOfNumber);
        bindReq.setAddressNpi(numberPlan);
        bindReq.setAddressRange(addressRange);

        try {
            if (bindTimeout > 0) {
                this.link.setTimeout(bindTimeout);
            }
        } catch (UnsupportedOperationException e) {
            GlobalConfiguration.getInstance().getLogger().warn("Link does not support read timeouts - bind timeout will not work");
        }

        this.setState(ConnectionState.BINDING);
        this.beforeWrite(bindReq);
        boolean success = super.write((Packet) bindReq);
        this.afterWrite(bindReq, success);
        return bindReq;
    }

    @Override
    public synchronized boolean unbind(boolean force) throws IOException, NotBoundException, BadCommandIDException {
        if (!force && (this.getState() != ConnectionState.BOUND || !this.link.isConnected())) {
            throw new NotBoundException();
        }

        this.setState(ConnectionState.UNBINDING);
        UnbindRequest u = (UnbindRequest) this.packetFactory.make(Packet.UNBIND, this);

        this.beforeWrite(u);
        boolean success = super.write((Packet) u);
        this.setState(ConnectionState.UNBOUND);
        this.afterWrite(u, success);

        return success;
    }

    protected Packet onRead(Packet packet) throws IOException {
        switch (packet.getCommandId()) {
            case Packet.DELIVER_SM:
                this.onDeliverSM((DeliverSMRequest) packet);
                break;

            case Packet.SUBMIT_SM_RESP:
                this.onSubmitSMResponse((SubmitSMResponse) packet);
                break;

            case Packet.SUBMIT_MULTI_RESP:
                this.onSubmitMultiResponse((SubmitMultiResponse) packet);
                break;

            case Packet.CANCEL_SM_RESP:
                this.onCancelSMResponse((CancelSMResponse) packet);
                break;

            case Packet.REPLACE_SM_RESP:
                this.onReplaceSMResponse((ReplaceSMResponse) packet);
                break;

            case Packet.PARAM_RETRIEVE_RESP:
                this.onParamRetrieveResponse((ParamRetrieveResponse) packet);
                break;

            case Packet.QUERY_SM_RESP:
                this.onQuerySMResponse((QuerySMResponse) packet);
                break;

            case Packet.QUERY_LAST_MSGS_RESP:
                this.onQueryLastMsgsResponse((QueryLastMsgsResponse) packet);
                break;

            case Packet.QUERY_MSG_DETAILS_RESP:
                this.onQueryMsgDetailsResponse((QueryMsgDetailsResponse) packet);
                break;

            case Packet.ENQUIRE_LINK:
                this.onEnquireLinkRequest((EnquireLinkRequest) packet);
                break;

            case Packet.ENQUIRE_LINK_RESP:
                this.onEnquireLinkResponse((EnquireLinkResponse) packet);
                break;

            case Packet.UNBIND:
                this.onUnBind((UnbindRequest) packet);
                break;

            case Packet.UNBIND_RESP:
                this.onUnBindResponse((UnbindResponse) packet);
                break;

            case Packet.BIND_TRANSMITTER_RESP:
            case Packet.BIND_TRANSCEIVER_RESP:
            case Packet.BIND_RECEIVER_RESP:
                this.onBindResponse((BindResponse) packet);
                break;

            case Packet.GENERIC_NACK:
                this.onGenericNack((GenericNackResponse) packet);
                break;

            default:
                this.onUnidentifiedPacket(packet);
                break;
        }

        if (null != this.getListener()) {
            this.getListener().onRead(packet, this);
        }

        return packet;
    }

    public void onCancelSMResponse(CancelSMResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onCancelSMResponse(this, packet);
        }
    }

    public void onDeliverSM(DeliverSMRequest packet) throws IOException {
        if (this.autoAcknowledgeDeliverSM) {
            DeliverSMResponse response = new DeliverSMResponse(packet);
            this.write(response);
        }

        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onDeliverSM(this, (DeliverSMRequest) packet);
        }
    }

    public void onParamRetrieveResponse(ParamRetrieveResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onParamRetrieveResponse(this, packet);
        }
    }

    public void onQuerySMResponse(QuerySMResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onQuerySMResponse(this, packet);
        }
    }

    public void onQueryLastMsgsResponse(QueryLastMsgsResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onQueryLastMsgsResponse(this, packet);
        }
    }

    public void onQueryMsgDetailsResponse(QueryMsgDetailsResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onQueryMsgDetailsResponse(this, packet);
        }
    }

    public void onReplaceSMResponse(ReplaceSMResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onReplaceSMResponse(this, packet);
        }
    }

    public void onSubmitSMResponse(SubmitSMResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onSubmitSMResponse(this, packet);
        }
    }

    public void onSubmitMultiResponse(SubmitMultiResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onSubmitMultiResponse(this, packet);
        }
    }

    public void onEnquireLinkRequest(EnquireLinkRequest packet) throws IOException {
        if (this.autoAcknowledgeQueryLinks) {
            EnquireLinkResponse response = new EnquireLinkResponse(packet);
            this.write(response);
        }

        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onEnquireLinkRequest(this, packet);
        }
    }

    public void onEnquireLinkResponse(EnquireLinkResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onEnquireLinkResponse(this, packet);
        }
    }

    public void onUnBind(UnbindRequest packet) throws IOException {
        this.setState(ConnectionState.UNBINDING);
        try {
            UnbindResponse ubr = (UnbindResponse) this.packetFactory.make(Packet.UNBIND_RESP, this);
            this.write(ubr);
        } catch (BadCommandIDException e) {
        } catch (VersionException e) {
        } catch (IOException e) {
            this.setState(ConnectionState.CLOSED);
        }

        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onUnBind(this, packet);
        }
    }

    public void onUnBindResponse(UnbindResponse packet) throws IOException {
        if (packet.getCommandStatus() == 0) {
            synchronized(this) {
                if( this.getState() != ConnectionState.CLOSED ) {
                    this.setState(ConnectionState.UNBOUND);
                }
            }
        }

        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onUnBindResponse(this, packet);
        }
    }

    public void onBindResponse(BindResponse packet) throws IOException {
        int st = packet.getCommandStatus();
        if (this.getState() != ConnectionState.BINDING) {
            GlobalConfiguration.getInstance().getLogger().warn("A bind response was received in bound state " + this.getState());
            throw new IllegalStateException("A bind response was received in bound state " + this.getState());
        }

        if (st != 0) {
            GlobalConfiguration.getInstance().getLogger().warn("Bind failed [{}], setting state to unbound.", SmppResponseCode.get(st).getDescription());

            switch( SmppResponseCode.get(st) ) {
                case INVBNDSTS:
                case BINDFAIL:
                    this.setState(ConnectionState.UNBOUND);
                    break;
                case ALYBND:
                    try {
                        this.unbind(true);
                    } catch( BadCommandIDException e) {
                    } catch( NotBoundException e) {
                        this.setState(ConnectionState.UNBOUND);
                    }
                    break;
            }

        } else {
            GlobalConfiguration.getInstance().getLogger().trace("SystemId = " + packet.getSystemId());
            ScInterfaceVersion intVersion = packet.getOptionalParameter(ScInterfaceVersion.class);
            Number n = intVersion == null ? null : intVersion.getValue();

            if (n == null) {
                GlobalConfiguration.getInstance().getLogger().trace("no sc_interface_version parameter was received using default version: " + SMPPVersion.getDefaultVersion().toString());
                n = new Integer(SMPPVersion.getDefaultVersion().getVersionID());
            }

            if (n != null) {
                SMPPVersion smscVersion = SMPPVersion.getVersion(n.intValue());
                GlobalConfiguration.getInstance().getLogger().trace("SMSC reports its supported SMPP version as " + smscVersion.toString());

                if (smscVersion.isOlder(this.version)) {
                    GlobalConfiguration.getInstance().getLogger().trace("Downgrading this connection's SMPP version to " + smscVersion.toString());
                }
                this.setVersion(smscVersion);
            } else {
                this.supportOptionalParams = false;
                GlobalConfiguration.getInstance().getLogger().trace("Disabling optional parameter support as no sc_interface_version parameter was received");
            }

            this.getLink().setTimeout(connectionTimeout);
            this.setState(ConnectionState.BOUND);
        }

        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onBindResponse(this, packet);
        }
    }

    public void onGenericNack(GenericNackResponse packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onGenericNack(this, packet);
        }
    }

    public void onUnidentifiedPacket(Packet packet) throws IOException {
        if (null != this.getInPacketListener()) {
            this.getInPacketListener().onUnidentifiedPacket(this, packet);
        }
    }

    protected Packet beforeWrite(Packet packet) throws IOException {
        try {
            int id = packet.getCommandId();

            if (!version.isSupported(id)) {
                throw new VersionException(version + " does not support command ID 0x" + Integer.toHexString(id));
            }

            switch (id) {
                case Packet.UNBIND_RESP:
                    this.processOutUnbindResponse((UnbindResponse) packet);
                    break;
                case Packet.BIND_RECEIVER:
                case Packet.BIND_TRANSCEIVER:
                case Packet.BIND_TRANSMITTER:
                case Packet.UNBIND:
                    break;
                default:
                    if (packet instanceof SMPPRequest) {
                        if (this.state != ConnectionState.BOUND) {
                            throw new IOException("Must be bound to the SMSC before sending packets");
                        }

                        if (this.type == ConnectionType.RECEIVER) {
                            if (id != Packet.ENQUIRE_LINK) {
                                throw new IOException("Operation not permitted over receiver connection");
                            }
                        }
                    }
            }

            return packet;
        } finally {
            if (null != this.getListener()) {
                this.getListener().beforeWrite(packet, this);
            }
        }

    }

    protected void afterWrite(Packet packet, boolean success) {
        if (null != this.getListener()) {
            this.getListener().afterWrite(packet, this, success);
        }
    }

    protected void processOutUnbindResponse(UnbindResponse unbindResp) {
        if (unbindResp.getCommandStatus() == 0) {
            this.setState(ConnectionState.UNBOUND);
        }
    }
}

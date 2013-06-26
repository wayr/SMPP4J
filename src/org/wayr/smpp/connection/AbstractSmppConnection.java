package org.wayr.smpp.connection;

import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.TimeUnit;
import org.wayr.smpp.Connection;
import org.wayr.smpp.GlobalConfiguration;
import org.wayr.smpp.Packet;
import org.wayr.smpp.PacketFactory;
import org.wayr.smpp.alphabet.AlphabetEncoding;
import org.wayr.smpp.alphabet.AlphabetEncodingFactory;
import org.wayr.smpp.alphabet.DefaultAlphabetEncodingFactory;
import org.wayr.smpp.alphabet.alphabets.GSMAlphabetEncoding;
import org.wayr.smpp.exceptions.AlreadyBoundException;
import org.wayr.smpp.exceptions.BadCommandIDException;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.exceptions.VersionException;
import org.wayr.smpp.net.Link;
import org.wayr.smpp.utils.BytesUtils;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.utils.IntegerSequence;
import org.wayr.smpp.version.SMPPVersion;

/**
 *
 * @author paul
 */
public abstract class AbstractSmppConnection extends Observable implements Connection {

    protected ConnectionType type;
    protected String systemId, password, systemType;
    //--
    protected int typeOfNumber = 0;
    protected int numberPlan = 0;
    protected String addressRange = null;
    //--
    protected ConnectionState state = ConnectionState.CLOSED;
    //--
    protected int bindTimeout = 30000;
    protected int connectionTimeout = 10000;
    //--
    protected IntegerSequence sequenceNumber = new IntegerSequence();
    //--
    protected Link link = null;
    //--
    protected SMPPVersion version = SMPPVersion.v34;
    protected AlphabetEncoding alphabet = new GSMAlphabetEncoding();
    protected AlphabetEncodingFactory alphabetFactory = new DefaultAlphabetEncodingFactory();
    //--
    protected PacketFactory packetFactory = new PacketFactory();
    //--
    protected boolean supportOptionalParams = this.version.isSupportOptionalParams();
    //--
    protected boolean autoAcknowledgeDeliverSM = true;
    protected boolean autoAcknowledgeQueryLinks = true;
    //--
    protected IOStreamConverter IOConverter = GlobalConfiguration.getInstance().getBufferConverter(); //new IOStreamConverter(this.numberConverter);

    public AbstractSmppConnection() {
    }

    public AbstractSmppConnection(Link connection) {
        this.link = connection;
    }

    public IntegerSequence getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(IntegerSequence sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public AlphabetEncodingFactory getAlphabetFactory() {
        return alphabetFactory;
    }

    public void setAlphabetFactory(AlphabetEncodingFactory alphabetFactory) {
        this.alphabetFactory = alphabetFactory;
    }

    public synchronized ConnectionState getState() {
        return state;
    }

    public synchronized void setState(ConnectionState state) {
        this.state = state;
    }

    public synchronized ConnectionType getType() {
        return type;
    }

    public synchronized void setType(ConnectionType type) {
        this.type = type;
    }

    public int getTypeOfNumber() {
        return typeOfNumber;
    }

    public void setTypeOfNumber(int typeOfNum) {
        this.typeOfNumber = typeOfNum;
    }

    public int getNumberPlan() {
        return numberPlan;
    }

    public void setNumberPlan(int numberPlan) {
        this.numberPlan = numberPlan;
    }

    public String getAddressRange() {
        return addressRange;
    }

    public void setAddressRange(String addressRange) {
        this.addressRange = addressRange;
    }

    public synchronized SMPPVersion getVersion() {
        return version;
    }

    public synchronized void setVersion(SMPPVersion version) throws VersionException {

        if (this.getState() != ConnectionState.CLOSED && this.getState() != ConnectionState.UNBOUND && this.getState() != ConnectionState.BINDING) {
            throw new VersionException("Cannot set SMPP Version after binding");
        }

        if (version == null) {
            version = SMPPVersion.getDefaultVersion();
        }

        this.version = version;
        this.supportOptionalParams = version.isSupportOptionalParams();
    }

    public synchronized int getBindTimeout() {
        return bindTimeout;
    }

    public synchronized void setBindTimeout(int bindTimeout) {
        this.bindTimeout = bindTimeout;
    }

    @Override
    public synchronized int getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public synchronized void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public synchronized IOStreamConverter getIOConverter() {
        return IOConverter;
    }

    public synchronized void setIOConverter(IOStreamConverter IOConverter) {
        this.IOConverter = IOConverter;
    }

    public synchronized AlphabetEncoding getAlphabet() {
        return alphabet;
    }

    public synchronized void setAlphabet(AlphabetEncoding alphabet) {
        this.alphabet = alphabet;
    }

    public synchronized boolean isAutoAcknowledgeDeliverSM() {
        return autoAcknowledgeDeliverSM;
    }

    public synchronized void setAutoAcknowledgeDeliverSM(boolean autoAcknowledgeDeliverSM) {
        this.autoAcknowledgeDeliverSM = autoAcknowledgeDeliverSM;
    }

    public synchronized boolean isAutoAcknowledgeQueryLinks() {
        return autoAcknowledgeQueryLinks;
    }

    public synchronized void setAutoAcknowledgeQueryLinks(boolean autoAcknowledgeQueryLinks) {
        this.autoAcknowledgeQueryLinks = autoAcknowledgeQueryLinks;
    }

    public synchronized PacketFactory getPacketFactory() {
        return packetFactory;
    }

    public synchronized void setPacketFactory(PacketFactory packetFactory) {
        this.packetFactory = packetFactory;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
    }

    public synchronized boolean isSupportOptionalParams() {
        return supportOptionalParams;
    }

    public synchronized void setSupportOptionalParams(boolean supportOptionalParams) {
        this.supportOptionalParams = supportOptionalParams;
    }

    public synchronized String getSystemId() {
        return systemId;
    }

    public synchronized void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public synchronized String getSystemType() {
        return systemType;
    }

    public synchronized void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    @Override
    public synchronized boolean open() throws Link.LinkNotUpException {
        try {
            if (this.link.open()) {
                this.setState(ConnectionState.OPEN);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new Link.LinkNotUpException(e);
        }
    }

    @Override
    public synchronized boolean isOpen() {
        return this.link.isConnected() && this.getState() != ConnectionState.CLOSED;
    }

    @Override
    public synchronized void close() {
        this.link.close();
        this.setState(ConnectionState.CLOSED);
    }

    @Override
    public Packet read() throws IOException {
        return this.readInternal();
    }

    @Override
    public Packet read(long l, TimeUnit tu) throws IOException, InterruptedException {
        int oldTimeout = this.getLink().getTimeout();
        try {
            Long timeout = tu.convert(l, TimeUnit.MILLISECONDS);
            this.getLink().setTimeout(timeout.intValue());

            return this.readInternal();
        } finally {
            this.getLink().setTimeout(oldTimeout);
        }
    }

    private Packet readInternal() throws IOException {
        Packet packet = null;

        byte[] buffer = new byte[1024];
        try {
            buffer = this.link.read(buffer);

            int len = this.IOConverter.readInt(buffer, 0);
            int id = this.IOConverter.readInt(buffer, 4);
            packet = this.packetFactory.make(id);

            if (packet != null) {
                packet.setAlphabetFactory(this.getAlphabetFactory());
                packet.fromBytes(buffer);

                // GlobalConfiguration.getInstance().getLogger().debug(packet.getClass().getSimpleName() + ":" + BytesUtils.bytesToHexString(buffer, 0, len));
            }

        } catch (BadCommandIDException e) {
            GlobalConfiguration.getInstance().getLogger().error(BytesUtils.bytesToHexString(buffer, 0, 1024));
            throw new SMPPProtocolException("Unrecognised command received", e);
        }
        return packet;
    }

    @Override
    public boolean write(Packet packet) throws IOException {
        return this.writeInternal(packet);
    }

    public byte[] convertToBytes(Packet packet) {
        byte[] tlv = new byte[0];
        if( packet.getTLVTable() != null ) {
            tlv = packet.getTLVTable().toBytes(IOConverter);
        }
        int len = 16 + packet.getBodyLength() + tlv.length;

        byte[] bytes = new byte[len];

        int offset = 0;
        offset = this.IOConverter.writeInt(bytes, offset, len);
        offset = this.IOConverter.writeInt(bytes, offset, packet.getCommandId());
        offset = this.IOConverter.writeInt(bytes, offset, packet.getCommandStatus());
        offset = this.IOConverter.writeInt(bytes, offset, packet.getSequenceNumber());

        if (this.supportOptionalParams) {
            offset = this.IOConverter.writeBytes(bytes, offset, packet.toBytes(IOConverter));
            this.IOConverter.writeBytes(bytes, offset, tlv);
        } else {
            this.IOConverter.writeBytes(bytes, offset, packet.toBytes(IOConverter));
        }
        return bytes;
    }

    private boolean writeInternal(Packet object) throws IOException {
        if (this.link == null) {
            throw new IOException("No SMSC connection.");
        }

        byte[] packet = this.convertToBytes(object);
        // GlobalConfiguration.getInstance().getLogger().debug(object.getClass().getSimpleName() + ":" + BytesUtils.bytesToHexString(packet));

        return this.link.write(packet);
    }

    public void reset() {
        if (this.getState() == ConnectionState.BOUND) {
            throw new AlreadyBoundException("Cannot reset connection while bound");
        }

        if (this.sequenceNumber != null) {
            this.sequenceNumber.reset();
        }
    }

    public Packet makePacket(int commandId) throws BadCommandIDException, VersionException {
        return this.packetFactory.make(commandId, this);
    }

    public <E extends Packet> E initializePacket(E packet) {
        packet.setVersion(this.getVersion());

        if (null != this.getSequenceNumber()) {
            packet.setSequenceNumber(this.getSequenceNumber().nextNumber());
        }

        if (null != this.getAlphabet()) {
            packet.setAlphabetEncoding(this.getAlphabet());
        }

        return packet;
    }
}

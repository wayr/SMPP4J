package org.wayr.smpp.connection;

import java.io.IOException;
import org.wayr.smpp.Packet;
import org.wayr.smpp.PacketFactory;
import org.wayr.smpp.alphabet.AlphabetEncoding;
import org.wayr.smpp.alphabet.AlphabetEncodingFactory;
import org.wayr.smpp.exceptions.VersionException;
import org.wayr.smpp.listeners.ConnectionClientListener;
import org.wayr.smpp.net.Link;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.utils.IntegerSequence;
import org.wayr.smpp.version.SMPPVersion;

/**
 *
 * @author paul
 */
public interface ISmppConnection {

    boolean open() throws Link.LinkNotUpException;

    void close() throws IOException;

    boolean isOpen();

    Packet read() throws IOException;

    boolean write(Packet packet) throws IOException;

    String getAddressRange();

    void setAddressRange(String addressRange);

    AlphabetEncoding getAlphabet();

    void setAlphabet(AlphabetEncoding alphabet);

    AlphabetEncodingFactory getAlphabetFactory();

    void setAlphabetFactory(AlphabetEncodingFactory alphabetFactory);

    int getBindTimeout();

    void setBindTimeout(int bindTimeout);

    int getConnectionTimeout();

    void setConnectionTimeout(int connectionTimeout);

    IOStreamConverter getIOConverter();

    void setIOConverter(IOStreamConverter IOConverter);

    ConnectionClientListener getListener();

    void setListener(ConnectionClientListener ioListener);

    int getNumberPlan();

    void setNumberPlan(int numberPlan);

    PacketFactory getPacketFactory();

    void setPacketFactory(PacketFactory packetFactory);

    String getPassword();

    void setPassword(String password);

    IntegerSequence getSequenceNumber();

    void setSequenceNumber(IntegerSequence sequenceNumber);

    ConnectionState getState();

    void setState(ConnectionState state);

    String getSystemId();

    void setSystemId(String systemId);

    String getSystemType();

    void setSystemType(String systemType);

    ConnectionType getType();

    void setType(ConnectionType type);

    int getTypeOfNumber();

    void setTypeOfNumber(int typeOfNumber);

    SMPPVersion getVersion();

    void setVersion(SMPPVersion version) throws VersionException;

    boolean isAutoAcknowledgeDeliverSM();

    void setAutoAcknowledgeDeliverSM(boolean autoAcknowledgeDeliverSM);

    boolean isAutoAcknowledgeQueryLinks();

    void setAutoAcknowledgeQueryLinks(boolean autoAcknowledgeQueryLinks);

    void setSupportOptionalParams(boolean supportOptionalParams);

    boolean isSupportOptionalParams();

    public byte[] convertToBytes(Packet packet);
}

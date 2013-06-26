package org.wayr.smpp;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Date;
import org.wayr.smpp.alphabet.AlphabetEncoding;
import org.wayr.smpp.alphabet.AlphabetEncodingFactory;
import org.wayr.smpp.alphabet.DefaultAlphabetEncodingFactory;
import org.wayr.smpp.exceptions.InvalidParameterValueException;
import org.wayr.smpp.exceptions.SMPPProtocolException;
import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.TLVTable;
import org.wayr.smpp.messages.tlv.Tag;
import org.wayr.smpp.utils.BytesSerializable;
import org.wayr.smpp.utils.BytesUtils;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.utils.MSBNumberToByteConverter;
import org.wayr.smpp.utils.SMPPDate;
import org.wayr.smpp.version.SMPPVersion;

/**
 *
 * @author paul
 */
public abstract class Packet implements BytesSerializable {

    /**
     * Command Id: Negative Acknowledgement
     */
    public static final int GENERIC_NACK = 0x80000000;
    /**
     * Command Id: Bind Receiver
     */
    public static final int BIND_RECEIVER = 0x00000001;
    /**
     * Command Id: Bind Receiver Response
     */
    public static final int BIND_RECEIVER_RESP = 0x80000001;
    /**
     * Command Id: Bind transmitter
     */
    public static final int BIND_TRANSMITTER = 0x00000002;
    /**
     * Command Id: Bind transmitter response
     */
    public static final int BIND_TRANSMITTER_RESP = 0x80000002;
    /**
     * Command Id: Query message
     */
    public static final int QUERY_SM = 0x00000003;
    /**
     * Command Id: Query message response
     */
    public static final int QUERY_SM_RESP = 0x80000003;
    /**
     * Command Id: Submit message
     */
    public static final int SUBMIT_SM = 0x00000004;
    /**
     * Command Id: Submit message response
     */
    public static final int SUBMIT_SM_RESP = 0x80000004;
    /**
     * Command Id: Deliver Short message
     */
    public static final int DELIVER_SM = 0x00000005;
    /**
     * Command Id: Deliver message response
     */
    public static final int DELIVER_SM_RESP = 0x80000005;
    /**
     * Command Id: Unbind
     */
    public static final int UNBIND = 0x00000006;
    /**
     * Command Id: Unbind response
     */
    public static final int UNBIND_RESP = 0x80000006;
    /**
     * Command Id: Replace message
     */
    public static final int REPLACE_SM = 0x00000007;
    /**
     * Command Id: replace message response
     */
    public static final int REPLACE_SM_RESP = 0x80000007;
    /**
     * Command Id: Cancel message
     */
    public static final int CANCEL_SM = 0x00000008;
    /**
     * Command Id: Cancel message response
     */
    public static final int CANCEL_SM_RESP = 0x80000008;
    /**
     * Command Id: Bind transceiver
     */
    public static final int BIND_TRANSCEIVER = 0x00000009;
    /**
     * Command Id: Bind transceiever response.
     */
    public static final int BIND_TRANSCEIVER_RESP = 0x80000009;
    /**
     * Command Id: Outbind.
     */
    public static final int OUTBIND = 0x0000000b;
    /**
     * Command Id: Enquire Link
     */
    public static final int ENQUIRE_LINK = 0x00000015;
    /**
     * Command Id: Enquire link respinse
     */
    public static final int ENQUIRE_LINK_RESP = 0x80000015;
    /**
     * Command Id: Submit multiple messages
     */
    public static final int SUBMIT_MULTI = 0x00000021;
    /**
     * Command Id: Submit multi response
     */
    public static final int SUBMIT_MULTI_RESP = 0x80000021;
    /**
     * Command Id: Parameter retrieve
     */
    public static final int PARAM_RETRIEVE = 0x00000022;
    /**
     * Command Id: Paramater retrieve response
     */
    public static final int PARAM_RETRIEVE_RESP = 0x80000022;
    /**
     * Command Id: Query last messages
     */
    public static final int QUERY_LAST_MSGS = 0x00000023;
    /**
     * Command Id: Query last messages response
     */
    public static final int QUERY_LAST_MSGS_RESP = 0x80000023;
    /**
     * Command Id: Query message details
     */
    public static final int QUERY_MSG_DETAILS = 0x00000024;
    /**
     * Command Id: Query message details response
     */
    public static final int QUERY_MSG_DETAILS_RESP = 0x80000024;
    /**
     * Command Id: alert notification.
     */
    public static final int ALERT_NOTIFICATION = 0x00000102;
    /**
     * Command Id: Data message.
     */
    public static final int DATA_SM = 0x00000103;
    /**
     * Command Id: Data message response.
     */
    public static final int DATA_SM_RESP = 0x80000103;
    public static final IOStreamConverter converter = new IOStreamConverter(new MSBNumberToByteConverter());
    protected SMPPVersion version;
    /**
     * Command ID.
     */
    protected int commandId;
    /**
     * Command status.
     */
    protected int commandStatus = 0;
    /**
     * Packet sequence number.
     */
    protected int sequenceNumber = 0;
    /**
     * Source address
     */
    protected Address source;
    /**
     * Destination address
     */
    protected Address destination;
    /**
     * The short message data
     */
    protected byte[] message;
    /**
     * Service type for this msg
     */
    protected String serviceType;
    /**
     * Scheduled delivery time
     */
    protected SMPPDate deliveryTime;
    /**
     * Scheduled expiry time
     */
    protected SMPPDate expiryTime;
    /**
     * Date of reaching final state
     */
    protected SMPPDate finalDate;
    /**
     * Smsc allocated message Id
     */
    protected String messageId;
    /**
     * Status of message
     */
    protected int messageStatus;
    /**
     * Error associated with message
     */
    protected int errorCode;
    /**
     * Message priority.
     */
    protected int priority;
    /**
     * Registered delivery.
     */
    protected int registered;
    /**
     * Replace if present.
     */
    protected int replaceIfPresent;
    /**
     * ESM class.
     */
    protected int esmClass;
    /**
     * GSM protocol ID.
     */
    protected int protocolID;
    /**
     * Default message number.
     */
    protected int defaultMsg;
    //--
    protected AlphabetEncoding alphabetEncoding;
    protected AlphabetEncodingFactory alphabetFactory = new DefaultAlphabetEncodingFactory();
    //--
    protected int dataCoding;
    protected static ByteArrayOutputStream bytesStream = new ByteArrayOutputStream();
    //--
    protected TLVTable tlvTable = new TLVTable();

    /**
     * Create a new SMPPPacket with specified Id.
     *
     * @param id Command Id value
     */
    protected Packet(int id) {
        this(id, 0);
    }

    /**
     * Create a new SMPPPacket with specified Id and sequence number.
     *
     * @param id Command Id value
     * @param seqNum Command Sequence number
     */
    protected Packet(int id, int seqNum) {
        this.commandId = id;
        this.sequenceNumber = seqNum;
    }

    protected Packet(int id, SMPPVersion version) {
        this.commandId = id;
        this.version = version;
    }

    protected Packet(int id, int seqNum, SMPPVersion version) {
        this.commandId = id;
        this.sequenceNumber = seqNum;
        this.version = version;
    }

    /**
     * Get the Command Id of this SMPP packet.
     *
     * @return The Command Id of this packet
     */
    public int getCommandId() {
        return commandId;
    }

    /**
     * Get the status of this packet.
     *
     * @return The error status of this packet (only relevent to Response
     * packets)
     */
    public int getCommandStatus() {
        return this.commandStatus;
    }

    /**
     * Get the sequence number of this packet.
     *
     * @return The sequence number of this SMPP packet
     */
    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public abstract int getBodyLength();

    public TLVTable setTLVTable(TLVTable table) {
        TLVTable t = this.tlvTable;
        if (table == null) {
            this.tlvTable = new TLVTable();
        } else {
            this.tlvTable = table;
        }

        return t;
    }

    public TLVTable getTLVTable() {
        return tlvTable;
    }

    public void setOptionalParameter(Tag tag, OptionalParameter value) {
        tlvTable.set(tag, value);
    }

    public <E extends OptionalParameter> E getOptionalParameter(Class<E> tagClass) {
        return tlvTable.get(tagClass);
    }

    public boolean isSet(Tag tag) {
        return tlvTable.isSet(tag);
    }

    public SMPPDate getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(SMPPDate d) {
        expiryTime = d;
    }

    public void setExpiryTime(Date d) {
        expiryTime = new SMPPDate(d);
    }

    public void setDeliveryTime(SMPPDate d) {
        this.deliveryTime = d;
    }

    public void setDeliveryTime(Date d) {
        this.deliveryTime = new SMPPDate(d);
    }

    public SMPPDate getDeliveryTime() {
        return deliveryTime;
    }

    public void setFinalDate(Date d) {
        this.finalDate = new SMPPDate(d);
    }

    public SMPPDate getFinalDate() {
        return finalDate;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public AlphabetEncoding getAlphabetEncoding() {
        return alphabetEncoding;
    }

    public void setAlphabetEncoding(AlphabetEncoding alphabetEncoding) {
        this.alphabetEncoding = alphabetEncoding;
        this.dataCoding = alphabetEncoding.getDataCoding();
    }

    public AlphabetEncodingFactory getAlphabetFactory() {
        return alphabetFactory;
    }

    public void setAlphabetFactory(AlphabetEncodingFactory alphabetFactory) {
        this.alphabetFactory = alphabetFactory;
    }

    public SMPPVersion getVersion() {
        return version;
    }

    public void setVersion(SMPPVersion version) {
        this.version = version;
    }

    public boolean isRequest() {
        return false;
    }

    public void setSource(Address s) throws InvalidParameterValueException {
        if (s != null) {
            if (version.validateAddress(s)) {
                this.source = s;
            } else {
                throw new InvalidParameterValueException("Bad source address.", s);
            }
        } else {
            this.source = null;
        }
    }

    public Address getSource() {
        return source;
    }

    public void setDestination(Address s) {
        if (s != null) {
            if (version.validateAddress(s)) {
                this.destination = s;
            } else {
                throw new InvalidParameterValueException("Bad destination address.", s);
            }
        } else {
            this.destination = null;
        }
    }

    public Address getDestination() {
        return destination;
    }

    public void setPriority(int p) throws InvalidParameterValueException {
        if (version.validatePriorityFlag(p)) {
            this.priority = p;
        } else {
            throw new InvalidParameterValueException("Bad priority flag value", p);
        }
    }

    public int getPriority() {
        return priority;
    }

    public void setRegistered(int r) throws InvalidParameterValueException {
        if (version.validateRegisteredDelivery(r)) {
            this.registered = r;
        } else {
            throw new InvalidParameterValueException("Bad registered delivery flag value", r);
        }
    }

    public int getRegistered() {
        return registered;
    }

    public void setReplaceIfPresent(int rip)
            throws InvalidParameterValueException {
        if (version.validateReplaceIfPresent(rip)) {
            this.replaceIfPresent = rip;
        } else {
            throw new InvalidParameterValueException("Bad replace if present flag value", rip);
        }
    }

    public int getReplaceIfPresent() {
        return replaceIfPresent;
    }

    public void setEsmClass(int c) throws InvalidParameterValueException {
        if (version.validateEsmClass(c)) {
            this.esmClass = c;
        } else {
            throw new InvalidParameterValueException("Bad ESM class", c);
        }
    }

    public int getEsmClass() {
        return esmClass;
    }

    public void setProtocolID(int id) throws InvalidParameterValueException {
        if (version.validateProtocolID(id)) {
            this.protocolID = id;
        } else {
            throw new InvalidParameterValueException("Bad Protocol ID", id);
        }
    }

    public int getProtocolID() {
        return protocolID;
    }

    public void setDataCoding(int dc) throws InvalidParameterValueException {
        if (version.validateDataCoding(dc)) {
            this.dataCoding = dc;
            if (dc > 0) {
                this.alphabetEncoding = alphabetFactory.getEncoding(dc);
            }
        } else {
            throw new InvalidParameterValueException("Bad data coding", dc);
        }
    }

    public int getDataCoding() {
        return dataCoding;
    }

    public void setDefaultMsg(int id) throws InvalidParameterValueException {
        if (version.validateDefaultMsg(id)) {
            this.defaultMsg = id;
        } else {
            throw new InvalidParameterValueException(
                    "Default message ID out of range", id);
        }
    }

    public int getDefaultMsg() {
        return defaultMsg;
    }

    public void setMessageText(String text) throws InvalidParameterValueException {
        this.setMessage(this.alphabetEncoding.encodeString(text), this.alphabetEncoding);
    }

    public void setMessageText(String text, AlphabetEncoding alphabet) throws InvalidParameterValueException {
        if (alphabet == null) {
            throw new NullPointerException("Alphabet cannot be null");
        }

        this.setMessage(alphabet.encodeString(text), alphabet);
    }

    public void setMessage(byte[] message) throws InvalidParameterValueException {
        this.setMessage(message, 0, message.length, null);
    }

    public void setMessage(byte[] message, AlphabetEncoding encoding) throws InvalidParameterValueException {
        this.setMessage(message, 0, message.length, encoding);
    }

    public byte[] toBytes() {
        return this.toBytes(converter);
    }

    public void fromBytes(byte[] buffer) {
        this.fromBytes(buffer, 0);
    }

    public void fromBytes(byte[] bytes, int offset) {

        int len = converter.readInt(bytes, offset);
        offset += 4;

        if (bytes.length < len) {
            throw new SMPPProtocolException("Header specifies the packet length is longer than the number of bytes available.");
        }

        int id = converter.readInt(bytes, offset);
        offset += 4;
        if (id != this.commandId) {
            throw new SMPPProtocolException("The packet on the input stream is not the same as this packet's type.");
        }

        this.commandStatus = converter.readInt(bytes, offset);
        offset += 4;

        this.sequenceNumber = converter.readInt(bytes, offset);
        offset += 4;

        offset = this.fromBytes(converter, bytes, offset, len);
        if( offset < len ) {
            this.getTLVTable().fromBytes(converter, bytes, offset, len);
        }

        if (dataCoding != 0) {
            alphabetEncoding = alphabetFactory.getEncoding(dataCoding);
        }

        if (alphabetEncoding == null) {
            alphabetEncoding = alphabetFactory.getDefaultAlphabet();
        }
    }

    public void setMessage(byte[] message, int start, int len, AlphabetEncoding encoding) throws InvalidParameterValueException {
        // encoding should never be null, but for resilience, we check it here
        // and default back to binary encoding if none is found.
        if (encoding == null) {
            encoding = this.alphabetFactory.getDefaultAlphabet(); // new BinaryEncoding();
        }

        int dcs = encoding.getDataCoding();

        if (message != null) {
            if ((start < 0) || (len < 0) || message.length < (start + len)) {
                throw new ArrayIndexOutOfBoundsException(
                        "Not enough bytes in array");
            }

            int encodedLength = len;
            int encodingLength = encoding.getEncodingLength();
            if (encodingLength != 8) {
                encodedLength = (len * encodingLength) / 8;
            }

            if (encodedLength > version.getMaxLength(SMPPVersion.MESSAGE_PAYLOAD)) {
                throw new InvalidParameterValueException("Message is too long : " + encodedLength + " > " + version.getMaxLength(SMPPVersion.MESSAGE_PAYLOAD), message);
            }

            this.message = new byte[len];
            System.arraycopy(message, start, this.message, 0, len);
            this.dataCoding = dcs;
        } else {
            this.message = null;
        }
    }

    public byte[] getMessage() {
        byte[] b = null;
        if (this.message != null) {
            b = new byte[this.message.length];
            System.arraycopy(this.message, 0, b, 0, b.length);
        }
        return b;
    }

    public String getMessageText() {
        return alphabetEncoding.decodeString(this.message);
    }

    public String getMessageText(AlphabetEncoding enc) {
        return enc.decodeString(this.message);
    }

    public int getMessageLen() {
        return (message == null) ? 0 : message.length;
    }

    public void setServiceType(String type) throws InvalidParameterValueException {
        if (type != null) {
            if (version.validateServiceType(type)) {
                this.serviceType = type;
            } else {
                throw new InvalidParameterValueException("Bad service type", type);
            }
        } else {
            this.serviceType = null;
        }
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setMessageId(String id) throws InvalidParameterValueException {
        if (id != null) {
            if (version.validateMessageId(id)) {
                this.messageId = id;
            } else {
                throw new InvalidParameterValueException("Bad message Id", id);
            }
        } else {
            this.messageId = null;
        }
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageStatus(int st) throws InvalidParameterValueException {
        if (version.validateMessageState(st)) {
            this.messageStatus = st;
        } else {
            throw new InvalidParameterValueException(
                    "Invalid message state", st);
        }
    }

    public int getMessageStatus() {
        return this.messageStatus;
    }

    public void setErrorCode(int code) throws InvalidParameterValueException {
        if (version.validateErrorCode(code)) {
            errorCode = code;
        } else {
            throw new InvalidParameterValueException("Invalid error code", code);
        }
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Packet other = (Packet) obj;
        if (this.version != other.version && (this.version == null || !this.version.equals(other.version))) {
            return false;
        }
        if (this.commandId != other.commandId) {
            return false;
        }
        if (this.commandStatus != other.commandStatus) {
            return false;
        }
        if (this.sequenceNumber != other.sequenceNumber) {
            return false;
        }
        if (this.source != other.source && (this.source == null || !this.source.equals(other.source))) {
            return false;
        }
        if (this.destination != other.destination && (this.destination == null || !this.destination.equals(other.destination))) {
            return false;
        }
        if (!Arrays.equals(this.message, other.message)) {
            return false;
        }
        if ((this.serviceType == null) ? (other.serviceType != null) : !this.serviceType.equals(other.serviceType)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (this.version != null ? this.version.hashCode() : 0);
        hash = 29 * hash + this.commandId;
        hash = 29 * hash + this.commandStatus;
        hash = 29 * hash + this.sequenceNumber;
        hash = 29 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 29 * hash + (this.destination != null ? this.destination.hashCode() : 0);
        hash = 29 * hash + Arrays.hashCode(this.message);
        hash = 29 * hash + (this.serviceType != null ? this.serviceType.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        byte[] bytes = this.toBytes();
        return this.getClass().getSimpleName() + ": " + BytesUtils.bytesToHexString(bytes);
    }
}

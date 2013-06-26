package org.wayr.smpp.messages.tlv;

import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.parameters.AdditionalStatusInfoText;
import org.wayr.smpp.messages.parameters.AlertOnMessageDelivery;
import org.wayr.smpp.messages.parameters.BillingIdentification;
import org.wayr.smpp.messages.parameters.CallbackNum;
import org.wayr.smpp.messages.parameters.CallbackNumAtag;
import org.wayr.smpp.messages.parameters.CallbackNumPresInd;
import org.wayr.smpp.messages.parameters.DeliveryFailureReason;
import org.wayr.smpp.messages.parameters.DestAddrSubunit;
import org.wayr.smpp.messages.parameters.DestBearerType;
import org.wayr.smpp.messages.parameters.DestNetworkType;
import org.wayr.smpp.messages.parameters.DestSubaddress;
import org.wayr.smpp.messages.parameters.DestTelematicsId;
import org.wayr.smpp.messages.parameters.DestinationPort;
import org.wayr.smpp.messages.parameters.DisplayTime;
import org.wayr.smpp.messages.parameters.DpfResult;
import org.wayr.smpp.messages.parameters.ItsReplyType;
import org.wayr.smpp.messages.parameters.ItsSessionInfo;
import org.wayr.smpp.messages.parameters.LanguageIndicator;
import org.wayr.smpp.messages.parameters.MessagePayload;
import org.wayr.smpp.messages.parameters.MessageState;
import org.wayr.smpp.messages.parameters.MoreMessagesToSend;
import org.wayr.smpp.messages.parameters.MsAvailabilityStatus;
import org.wayr.smpp.messages.parameters.MsMsgWaitFacilities;
import org.wayr.smpp.messages.parameters.MsValidity;
import org.wayr.smpp.messages.parameters.NetworkErrorCode;
import org.wayr.smpp.messages.parameters.NumberOfMessages;
import org.wayr.smpp.messages.parameters.PayloadType;
import org.wayr.smpp.messages.parameters.PrivacyIndicator;
import org.wayr.smpp.messages.parameters.QosTimeToLive;
import org.wayr.smpp.messages.parameters.ReceiptedMessageId;
import org.wayr.smpp.messages.parameters.SarMsgRefNum;
import org.wayr.smpp.messages.parameters.SarSegmentSeqnum;
import org.wayr.smpp.messages.parameters.SarTotalSegments;
import org.wayr.smpp.messages.parameters.ScInterfaceVersion;
import org.wayr.smpp.messages.parameters.SetDpf;
import org.wayr.smpp.messages.parameters.SmsSignal;
import org.wayr.smpp.messages.parameters.SourceAddrSubunit;
import org.wayr.smpp.messages.parameters.SourceBearerType;
import org.wayr.smpp.messages.parameters.SourceNetworkType;
import org.wayr.smpp.messages.parameters.SourcePort;
import org.wayr.smpp.messages.parameters.SourceSubaddress;
import org.wayr.smpp.messages.parameters.SourceTelematicsId;
import org.wayr.smpp.messages.parameters.UserMessageReference;
import org.wayr.smpp.messages.parameters.UserResponseCode;
import org.wayr.smpp.messages.parameters.UssdServiceOp;
import org.wayr.smpp.messages.parameters.VendorSpecificDestMscAddr;
import org.wayr.smpp.messages.parameters.VendorSpecificSourceMscAddr;

/**
 *
 * @author paul
 */
public enum Tag {

    DEST_ADDR_SUBUNIT(0x0005, DestAddrSubunit.class),
    DEST_NETWORK_TYPE(0x0006, DestNetworkType.class),
    DEST_BEARER_TYPE(0x0007, DestBearerType.class),
    DEST_TELEMATICS_ID(0x0008, DestTelematicsId.class),
    SOURCE_ADDR_SUBUNIT(0x000D, SourceAddrSubunit.class),
    SOURCE_NETWORK_TYPE(0x000E, SourceNetworkType.class),
    SOURCE_BEARER_TYPE(0x000F, SourceBearerType.class),
    SOURCE_TELEMATICS_ID(0x0010, SourceTelematicsId.class),
    QOS_TIME_TO_LIVE(0x0017, QosTimeToLive.class),
    PAYLOAD_TYPE(0x0019, PayloadType.class),
    ADDITIONAL_STATUS_INFO_TEXT(0x001D, AdditionalStatusInfoText.class),
    RECEIPTED_MESSAGE_ID(0x001E, ReceiptedMessageId.class),
    MS_MSG_WAIT_FACILITIES(0x0030, MsMsgWaitFacilities.class),
    PRIVACY_INDICATOR(0x0201, PrivacyIndicator.class),
    SOURCE_SUBADDRESS(0x0202, SourceSubaddress.class),
    DEST_SUBADDRESS(0x0203, DestSubaddress.class),
    USER_MESSAGE_REFERENCE(0x0204, UserMessageReference.class),
    USER_RESPONSE_CODE(0x0205, UserResponseCode.class),
    SOURCE_PORT(0x020A, SourcePort.class),
    DESTINATION_PORT(0x020B, DestinationPort.class),
    SAR_MSG_REF_NUM(0x020C, SarMsgRefNum.class),
    LANGUAGE_INDICATOR(0x020D, LanguageIndicator.class),
    SAR_TOTAL_SEGMENTS(0x020E, SarTotalSegments.class),
    SAR_SEGMENT_SEQNUM(0x020F, SarSegmentSeqnum.class),
    SC_INTERFACE_VERSION(0x0210, ScInterfaceVersion.class),
    CALLBACK_NUM_PRES_IND(0x0302, CallbackNumPresInd.class),
    CALLBACK_NUM_ATAG(0x0303, CallbackNumAtag.class),
    NUMBER_OF_MESSAGES(0x0304, NumberOfMessages.class),
    CALLBACK_NUM(0x0381, CallbackNum.class),
    DPF_RESULT(0x0420, DpfResult.class),
    SET_DPF(0x0421, SetDpf.class),
    MS_AVAILABILITY_STATUS(0x0422, MsAvailabilityStatus.class),
    NETWORK_ERROR_CODE(0x0423, NetworkErrorCode.class),
    MESSAGE_PAYLOAD(0x0424, MessagePayload.class),
    DELIVERY_FAILURE_REASON(0x0425, DeliveryFailureReason.class),
    MORE_MESSAGES_TO_SEND(0x0426, MoreMessagesToSend.class),
    MESSAGE_STATE(0x0427, MessageState.class),
    USSD_SERVICE_OP(0x0501, UssdServiceOp.class),
    BILLING_IDENTIFICATION(0x060B, BillingIdentification.class),
    DISPLAY_TIME(0x1201, DisplayTime.class),
    SMS_SIGNAL(0x1203, SmsSignal.class),
    MS_VALIDITY(0x1204, MsValidity.class),
    ALERT_ON_MESSAGE_DELIVERY(0x130C, AlertOnMessageDelivery.class),
    ITS_REPLY_TYPE(0x1380, ItsReplyType.class),
    ITS_SESSION_INFO(0x1383, ItsSessionInfo.class),
    VENDOR_SPECIFIC_SOURCE_MSC_ADDR(0x1501, VendorSpecificSourceMscAddr.class),
    VENDOR_SPECIFIC_DEST_MSC_ADDR(0x1502, VendorSpecificDestMscAddr.class);
    private final short code;
    final Class<? extends OptionalParameter> type;
    private static final Logger logger = LoggerFactory.getLogger(Tag.class);

    private Tag(int code, Class<? extends OptionalParameter> type) {
        this.code = (short) code;
        this.type = type;
    }

    /**
     * Get the tag code of the {@link Tag}.
     *
     * @returns the tag code.
     * @deprecated use {@link #code()}
     */
    @Deprecated
    public short value() {
        return code;
    }

    /**
     * Get the tag code of the {@link Tag}.
     *
     * @returns the tag code.
     */
    public short code() {
        return code;
    }

    public Class<? extends OptionalParameter> type() {
        return type;
    }

    public OptionalParameter newInstance() {
        try {
            return type.newInstance();
        } catch( IllegalAccessException e) {
            logger.error("tag {} cant be instanciate", code, e);
            return new OptionalParameter.COctetStringParameter(code);
        } catch( IllegalArgumentException e) {
            logger.error("tag {} cant be instanciate", code, e);
            return new OptionalParameter.COctetStringParameter(code);
        } catch( InstantiationException e) {
            logger.error("tag {} cant be instanciate", code, e);
            return new OptionalParameter.COctetStringParameter(code);
        } catch( SecurityException e ) {
            logger.error("tag {} cant be instanciate", code, e);
            return new OptionalParameter.COctetStringParameter(code);
        }
    }

    public static Tag valueOf(short code) {
        for (Tag tag : Tag.values()) {
            if (tag.code == code) {
                return tag;
            }
        }
        return null;
    }
}

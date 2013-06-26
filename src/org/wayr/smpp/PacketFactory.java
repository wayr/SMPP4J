package org.wayr.smpp;

import org.wayr.smpp.connection.AbstractSmppConnection;
import org.wayr.smpp.messages.BindReceiverRequest;
import org.wayr.smpp.messages.QueryMsgDetailsResponse;
import org.wayr.smpp.messages.GenericNackResponse;
import org.wayr.smpp.messages.UnbindRequest;
import org.wayr.smpp.messages.QueryLastMsgsRequest;
import org.wayr.smpp.messages.BindTransceiverResponse;
import org.wayr.smpp.messages.ParamRetrieveRequest;
import org.wayr.smpp.messages.BindTransmitterResponse;
import org.wayr.smpp.messages.DeliverSMRequest;
import org.wayr.smpp.messages.SubmitSMResponse;
import org.wayr.smpp.messages.DataSMRequest;
import org.wayr.smpp.messages.QueryLastMsgsResponse;
import org.wayr.smpp.messages.EnquireLinkRequest;
import org.wayr.smpp.messages.ReplaceSMRequest;
import org.wayr.smpp.messages.QuerySMResponse;
import org.wayr.smpp.messages.EnquireLinkResponse;
import org.wayr.smpp.messages.DeliverSMResponse;
import org.wayr.smpp.messages.SubmitMultiRequest;
import org.wayr.smpp.messages.QueryMsgDetailsRequest;
import org.wayr.smpp.messages.CancelSMResponse;
import org.wayr.smpp.messages.AlertNotification;
import org.wayr.smpp.messages.BindTransmitterRequest;
import org.wayr.smpp.messages.QuerySMRequest;
import org.wayr.smpp.messages.SubmitSMRequest;
import org.wayr.smpp.messages.BindTransceiverRequest;
import org.wayr.smpp.messages.ParamRetrieveResponse;
import org.wayr.smpp.messages.CancelSMRequest;
import org.wayr.smpp.messages.BindReceiverResponse;
import org.wayr.smpp.messages.ReplaceSMResponse;
import org.wayr.smpp.messages.SubmitMultiResponse;
import org.wayr.smpp.messages.UnbindResponse;
import org.wayr.smpp.messages.DataSMResponse;
import org.wayr.smpp.exceptions.BadCommandIDException;
import org.wayr.smpp.exceptions.VersionException;

/**
 *
 * @author paul
 */
public class PacketFactory {

    public PacketFactory() {
    }

    public Packet make(int id) throws BadCommandIDException {
        Packet response = null;

        switch (id) {
            case Packet.GENERIC_NACK:
                response = new GenericNackResponse();
                break;

            case Packet.BIND_RECEIVER:
                response = new BindReceiverRequest();
                break;

            case Packet.BIND_RECEIVER_RESP:
                response = new BindReceiverResponse();
                break;

            case Packet.BIND_TRANSMITTER:
                response = new BindTransmitterRequest();
                break;

            case Packet.BIND_TRANSMITTER_RESP:
                response = new BindTransmitterResponse();
                break;

            case Packet.BIND_TRANSCEIVER:
                response = new BindTransceiverRequest();
                break;

            case Packet.BIND_TRANSCEIVER_RESP:
                response = new BindTransceiverResponse();
                break;

            case Packet.UNBIND:
                response = new UnbindRequest();
                break;

            case Packet.UNBIND_RESP:
                response = new UnbindResponse();
                break;

            case Packet.SUBMIT_SM:
                response = new SubmitSMRequest();
                break;

            case Packet.SUBMIT_SM_RESP:
                response = new SubmitSMResponse();
                break;

            case Packet.DATA_SM:
                response = new DataSMRequest();
                break;

            case Packet.DATA_SM_RESP:
                response = new DataSMResponse();
                break;

            case Packet.ALERT_NOTIFICATION:
                response = new AlertNotification();
                break;

            case Packet.SUBMIT_MULTI:
                response = new SubmitMultiRequest();
                break;

            case Packet.SUBMIT_MULTI_RESP:
                response = new SubmitMultiResponse();
                break;

            case Packet.DELIVER_SM:
                response = new DeliverSMRequest();
                break;

            case Packet.DELIVER_SM_RESP:
                response = new DeliverSMResponse();
                break;

            case Packet.QUERY_SM:
                response = new QuerySMRequest();
                break;

            case Packet.QUERY_SM_RESP:
                response = new QuerySMResponse();
                break;

            case Packet.QUERY_LAST_MSGS:
                response = new QueryLastMsgsRequest();
                break;

            case Packet.QUERY_LAST_MSGS_RESP:
                response = new QueryLastMsgsResponse();
                break;

            case Packet.QUERY_MSG_DETAILS:
                response = new QueryMsgDetailsRequest();
                break;

            case Packet.QUERY_MSG_DETAILS_RESP:
                response = new QueryMsgDetailsResponse();
                break;

            case Packet.CANCEL_SM:
                response = new CancelSMRequest();
                break;

            case Packet.CANCEL_SM_RESP:
                response = new CancelSMResponse();
                break;

            case Packet.REPLACE_SM:
                response = new ReplaceSMRequest();
                break;

            case Packet.REPLACE_SM_RESP:
                response = new ReplaceSMResponse();
                break;

            case Packet.ENQUIRE_LINK:
                response = new EnquireLinkRequest();
                break;

            case Packet.ENQUIRE_LINK_RESP:
                response = new EnquireLinkResponse();
                break;

            case Packet.PARAM_RETRIEVE:
                response = new ParamRetrieveRequest();
                break;

            case Packet.PARAM_RETRIEVE_RESP:
                response = new ParamRetrieveResponse();
                break;

            default:
                throw new BadCommandIDException();
        }
        return response;
    }

    public Packet make(int commandId, AbstractSmppConnection connection) throws BadCommandIDException, VersionException {

        if (!connection.getVersion().isSupported(commandId)) {
            throw new VersionException("Command [" + commandId + "] not supported in version " + connection.getVersion());
        }

        Packet packet = this.make(commandId);
        packet.setVersion(connection.getVersion());

        if (null != connection.getSequenceNumber()) {
            packet.setSequenceNumber(connection.getSequenceNumber().nextNumber());
        }

        if (null != connection.getAlphabet()) {
            packet.setAlphabetEncoding(connection.getAlphabet());
        }

        return packet;
    }
}

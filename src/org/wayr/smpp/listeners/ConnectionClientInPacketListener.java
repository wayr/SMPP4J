package org.wayr.smpp.listeners;

import java.io.IOException;
import org.wayr.smpp.Packet;
import org.wayr.smpp.connection.SmppConnectionClient;
import org.wayr.smpp.messages.BindResponse;
import org.wayr.smpp.messages.CancelSMResponse;
import org.wayr.smpp.messages.DeliverSMRequest;
import org.wayr.smpp.messages.EnquireLinkRequest;
import org.wayr.smpp.messages.EnquireLinkResponse;
import org.wayr.smpp.messages.GenericNackResponse;
import org.wayr.smpp.messages.ParamRetrieveResponse;
import org.wayr.smpp.messages.QueryLastMsgsResponse;
import org.wayr.smpp.messages.QueryMsgDetailsResponse;
import org.wayr.smpp.messages.QuerySMResponse;
import org.wayr.smpp.messages.ReplaceSMResponse;
import org.wayr.smpp.messages.SubmitMultiResponse;
import org.wayr.smpp.messages.SubmitSMResponse;
import org.wayr.smpp.messages.UnbindRequest;
import org.wayr.smpp.messages.UnbindResponse;

/**
 *
 * @author paul
 */
public interface ConnectionClientInPacketListener {

    void onBindResponse(SmppConnectionClient source, BindResponse packet) throws IOException;

    void onCancelSMResponse(SmppConnectionClient source, CancelSMResponse packet) throws IOException;

    void onDeliverSM(SmppConnectionClient source, DeliverSMRequest packet) throws IOException;

    void onEnquireLinkRequest(SmppConnectionClient source, EnquireLinkRequest packet) throws IOException;

    void onEnquireLinkResponse(SmppConnectionClient source, EnquireLinkResponse packet) throws IOException;

    void onParamRetrieveResponse(SmppConnectionClient source, ParamRetrieveResponse packet) throws IOException;

    void onQuerySMResponse(SmppConnectionClient source, QuerySMResponse packet) throws IOException;

    void onQueryLastMsgsResponse(SmppConnectionClient source, QueryLastMsgsResponse packet) throws IOException;

    void onQueryMsgDetailsResponse(SmppConnectionClient source, QueryMsgDetailsResponse packet) throws IOException;

    void onReplaceSMResponse(SmppConnectionClient source, ReplaceSMResponse packet) throws IOException;

    void onSubmitSMResponse(SmppConnectionClient source, SubmitSMResponse packet) throws IOException;

    void onSubmitMultiResponse(SmppConnectionClient source, SubmitMultiResponse packet) throws IOException;

    void onUnBind(SmppConnectionClient source, UnbindRequest packet) throws IOException;

    void onUnBindResponse(SmppConnectionClient source, UnbindResponse packet) throws IOException;

    void onGenericNack(SmppConnectionClient source, GenericNackResponse packet) throws IOException;

    void onUnidentifiedPacket(SmppConnectionClient source, Packet packet) throws IOException;
}

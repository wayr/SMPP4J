/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.wayr.smpp.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.wayr.smpp.messages.EmptyResponse;
import org.wayr.smpp.messages.DeliverSMResponse;
import org.wayr.smpp.messages.SubmitMultiRequest;
import org.wayr.smpp.messages.QueryMsgDetailsResponse;
import org.wayr.smpp.messages.QueryMsgDetailsRequest;
import org.wayr.smpp.messages.BindResponse;
import org.wayr.smpp.messages.QueryLastMsgsRequest;
import org.wayr.smpp.messages.EmptyRequest;
import org.wayr.smpp.messages.ParamRetrieveRequest;
import org.wayr.smpp.messages.QuerySMRequest;
import org.wayr.smpp.messages.SubmitSMRequest;
import org.wayr.smpp.messages.SMPPResponse;
import org.wayr.smpp.messages.SubmitSMResponse;
import org.wayr.smpp.messages.DeliverSMRequest;
import org.wayr.smpp.messages.ParamRetrieveResponse;
import org.wayr.smpp.messages.DataSMRequest;
import org.wayr.smpp.messages.CancelSMRequest;
import org.wayr.smpp.messages.QueryLastMsgsResponse;
import org.wayr.smpp.messages.ReplaceSMRequest;
import org.wayr.smpp.messages.SubmitMultiResponse;
import org.wayr.smpp.messages.QuerySMResponse;
import org.wayr.smpp.messages.BindRequest;
import org.wayr.smpp.messages.DataSMResponse;
import org.wayr.smpp.Address;
import org.wayr.smpp.ErrorAddress;
import org.wayr.smpp.Packet;

/**
 *
 * @author paul
 */
public class PacketSerializer {

    protected Packet packet;
    protected int sequenceNumber = 0;
    protected Date createdAt = new Date();
    protected Map<String, Object> request = new LinkedHashMap<String, Object>();
    protected String requestBytes = "";
    protected Map<String, Object> response = new LinkedHashMap<String, Object>();
    protected String responseBytes = "";

    public PacketSerializer() {
    }

    public PacketSerializer(Packet packet) {
        this.packet = packet;
        this.sequenceNumber = packet.getSequenceNumber();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Map<String, Object> getRequest() {
        return request;
    }

    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    public String getRequestBytes() {
        return requestBytes;
    }

    public void setRequestBytes(String requestBytes) {
        this.requestBytes = requestBytes;
    }

    public Map<String, Object> getResponse() {
        return response;
    }

    public void setResponse(Map<String, Object> response) {
        this.response = response;
    }

    public String getResponseBytes() {
        return responseBytes;
    }

    public void setResponseBytes(String responseBytes) {
        this.responseBytes = responseBytes;
    }

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;

        if (packet instanceof SMPPResponse) {
            this.response = this.toMap(packet);
            this.responseBytes = BytesUtils.bytesToHexString(packet.toBytes());
        } else {
            this.request = this.toMap(packet);
            this.requestBytes = BytesUtils.bytesToHexString(packet.toBytes());
        }
    }

    @Override
    public String toString() {
        Map<String, Object> map = this.toMap(this.packet);
        return (map == null) ? null : map.toString();
    }

    public Map<String, Object> toMap(Packet pak) {

        Object result = this.apply(this, "getFields", new Object[]{pak});
        String[] keys = (String[]) result;
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        Map<String, Object> mapPacket = new LinkedHashMap<String, Object>();
        Map<String, Object> values = this.getDefaultValue(pak);

        mapPacket.put("class", pak.getClass().getSimpleName());
        mapPacket.put("command_id", String.format("%02X", pak.getCommandId()));
        mapPacket.put("command_status", pak.getCommandStatus());
        mapPacket.put("sequence", pak.getSequenceNumber());

        for (String key : keys) {
            mapPacket.put(key, values.get(key));
        }
        map.put("packet", mapPacket);
        map.put("bytes", BytesUtils.bytesToHexString(this.packet.toBytes()));
        return map;
    }

    public Map<String, Object> getDefaultValue(Packet packet) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("service_type", packet.getServiceType());

        map.put("source", packet.getSource() == null ? null : packet.getSource().getAddress());
        map.put("destination", packet.getDestination() == null ? null : packet.getDestination().getAddress());


        map.put("message", packet.getMessage() == null ? null : new String(packet.getMessage()));

        map.put("message_id", packet.getMessageId());
        map.put("message_status", packet.getMessageStatus());

        map.put("error_code", packet.getErrorCode());

        map.put("priority", packet.getPriority());
        map.put("registered", packet.getRegistered());
        map.put("replace", packet.getReplaceIfPresent());
        map.put("esm_class", packet.getEsmClass());
        map.put("protocol_id", packet.getProtocolID());
        map.put("default_msg", packet.getDefaultMsg());
        map.put("data_coding", packet.getDataCoding());

        map.put("delivery_time", packet.getDeliveryTime() == null ? null : packet.getDeliveryTime().toString());
        map.put("expiry_time", packet.getExpiryTime() == null ? null : packet.getExpiryTime().toString());
        map.put("final_time", packet.getFinalDate() == null ? null : packet.getFinalDate().toString());


        map.put("version", packet.getVersion() == null ? null : packet.getVersion().getVersionID());

        if (packet instanceof QueryMsgDetailsResponse) {
            List<Object> destinations = new LinkedList<Object>();
            Iterator it = ((QueryMsgDetailsResponse) packet).getDestinationTable().iterator();
            while (it.hasNext()) {
                Object dest = it.next();
                if (dest instanceof Address) {
                    destinations.add(((Address) dest).getAddress());
                } else {
                    destinations.add(dest);
                }
            }
            map.put("destinations", destinations);
        }

        if (packet instanceof SubmitMultiRequest) {
            List<Object> destinations = new LinkedList<Object>();
            Iterator it = ((SubmitMultiRequest) packet).getDestinationTable().iterator();
            while (it.hasNext()) {
                Object dest = it.next();
                if (dest instanceof Address) {
                    destinations.add(((Address) dest).getAddress());
                } else {
                    destinations.add(dest);
                }
            }
            map.put("destinations", destinations);
        }

        if (packet instanceof SubmitMultiResponse) {
            List<Object> list = new LinkedList<Object>();
            Iterator<ErrorAddress> it = ((SubmitMultiResponse) packet).getUnsuccessfullTable().iterator();
            while (it.hasNext()) {
                ErrorAddress obj = it.next();
                list.add(obj.getError());
            }
            map.put("unsuccessfull", list);
        }

        if (packet instanceof QueryLastMsgsRequest) {
            map.put("msg_count", ((QueryLastMsgsRequest) packet).getMsgCount());
        }

        if (packet instanceof QueryLastMsgsResponse) {
            List<Object> list = new LinkedList<Object>();
            for (String id : ((QueryLastMsgsResponse) packet).getMessageIds()) {
                list.add(id);
            }
            map.put("message_ids", list);
        }

        if (packet instanceof ParamRetrieveRequest) {
            map.put("param_name", ((ParamRetrieveRequest) packet).getParamName());
        }

        if (packet instanceof ParamRetrieveResponse) {
            map.put("param_value", ((ParamRetrieveResponse) packet).getParamValue());
        }

        if (packet instanceof BindResponse) {
            map.put("system_id", ((BindResponse) packet).getSystemId());
        }

        if (packet instanceof BindRequest) {
            BindRequest q = (BindRequest) packet;
            map.put("system_id", q.getSystemId());
            map.put("password", q.getPassword());
            map.put("system_type", q.getSystemType());
            map.put("address_range", q.getAddressRange());
            map.put("address_ton", q.getAddressTon());
            map.put("address_npi", q.getAddressNpi());
        }
        return map;
    }

    public String[] getFields(EmptyResponse r) {
        return new String[]{};
    }

    public String[] getFields(EmptyRequest r) {
        return new String[]{};
    }

    public String[] getFields(SubmitSMRequest r) {
        return new String[]{
            "service_type",
            "source",
            "destination",
            "esm_class",
            "protocol_id",
            "priority",
            "delivery_time",
            "expiry_time",
            "registered",
            "replace",
            "data_coding",
            "default_msg",
            "message"
        };
    }

    public String[] getFields(SubmitSMResponse r) {
        return new String[]{"message_id"};
    }

    public String[] getFields(ReplaceSMRequest r) {
        return new String[]{
            "message_id",
            "source",
            "delivery_time",
            "expiry_time",
            "esm_class",
            "registered",
            "default_msg",
            "message"
        };
    }

    public String[] getFields(QuerySMResponse r) {
        return new String[]{
            "message_id",
            "source",
            "destination",
            "esm_class",
            "protocol_id",
            "priority",
            "delivery_time",
            "expiry_time",
            "registered",
            "replace",
            "data_coding",
            "default_msg",
            "message"
        };
    }

    public String[] getFields(QuerySMRequest r) {
        return new String[]{
            "message_id",
            "source"
        };
    }

    public String[] getFields(QueryMsgDetailsResponse r) {
        return new String[]{
            "service_type",
            "source",
            "destinations",
            "delivery_time",
            "protocol_id",
            "priority",
            "expiry_time",
            "final_time",
            "registered",
            "data_coding",
            "message",
            "message_id",
            "final_time",
            "message_status",
            "error_code"
        };
    }

    public String[] getFields(QueryMsgDetailsRequest r) {
        return new String[]{
            "message_id",
            "source"
        };
    }

    public String[] getFields(DeliverSMResponse r) {
        return new String[]{
            "message_id",};
    }

    public String[] getFields(DeliverSMRequest r) {
        return new String[]{
            "service_type",
            "source",
            "destination",
            "esm_class",
            "protocol_id",
            "priority",
            "delivery_time",
            "expiry_time",
            "registered",
            "replace",
            "data_coding",
            "default_msg",
            "message"
        };
    }

    public String[] getFields(DataSMResponse r) {
        return new String[]{
            "message_id",};
    }

    public String[] getFields(DataSMRequest r) {
        return new String[]{
            "service_type",
            "source",
            "destination",
            "esm_class",
            "registered",
            "data_coding"
        };
    }

    public String[] getFields(CancelSMRequest r) {
        return new String[]{
            "service_type",
            "message_id",
            "source",
            "destination"
        };
    }

    public String[] getFields(BindResponse r) {
        return new String[]{
            "system_id",};
    }

    public String[] getFields(BindRequest r) {
        return new String[]{
            "system_id",
            "password",
            "system_type",
            "version",
            "address_ton",
            "address_npi",
            "address_range",};
    }

    public String[] getFields(SubmitMultiRequest r) {
        return new String[]{
            "service_type",
            "source",
            "destinations",
            "esm_class",
            "protocol_id",
            "priority",
            "delivery_time",
            "expiry_time",
            "registered",
            "replace",
            "data_coding",
            "default_msg",
            "message"
        };
    }

    public String[] getFields(SubmitMultiResponse r) {
        return new String[]{
            "message_id",
            "unsuccess"
        };
    }

    public String[] getFields(QueryLastMsgsRequest r) {
        return new String[]{
            "source",
            "msg_count"
        };
    }

    public String[] getFields(QueryLastMsgsResponse r) {
        return new String[]{
            "source",
            "message_ids"
        };
    }

    public String[] getFields(ParamRetrieveRequest r) {
        return new String[]{
            "param_name"
        };
    }

    public String[] getFields(ParamRetrieveResponse r) {
        return new String[]{
            "param_value"
        };
    }

    public Object apply(Object object, String methodName, Object[] parameters) {
        if (methodName == null) {
            return null;
        }

        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            try {
                if (methodName.equals(method.getName())) {
                    Class[] types = method.getParameterTypes();
                    if (types.length == parameters.length) {
                        Object[] p = new Object[types.length];
                        for (int i = 0; i < types.length; i++) {
                            Class t = types[i];
                            try {
                                p[i] = t.cast(parameters[i]);
                            } catch (ClassCastException e) {
                                throw e;
                            }
                        }

                        try {
                            return method.invoke(object, p);
                        } catch (IllegalAccessException e) {
                        } catch (IllegalArgumentException e) {
                        } catch (InvocationTargetException e) {
                        }
                    }
                }
            } catch (ClassCastException e) {
            }
        }

        throw new RuntimeException("No " + object.getClass() + "." + methodName + "(" + BytesUtils.join(parameters, ", ", 1) + ") found.");
    }
}

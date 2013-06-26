package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class QosTimeToLive extends OptionalParameter.IntegerParameter {

    public QosTimeToLive() {
        super(Tag.QOS_TIME_TO_LIVE.code());
    }

    public QosTimeToLive(Short tag, Integer value) {
        super(tag, value);
    }

    public QosTimeToLive(Tag tag, Integer value) {
        super(tag, value);
    }
}
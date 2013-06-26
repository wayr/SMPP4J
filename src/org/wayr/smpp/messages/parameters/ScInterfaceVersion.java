package org.wayr.smpp.messages.parameters;

import org.wayr.smpp.messages.OptionalParameter;
import org.wayr.smpp.messages.tlv.Tag;

/**
 *
 * @author paul
 */
public class ScInterfaceVersion extends OptionalParameter.ByteParameter {

    public ScInterfaceVersion() {
        super(Tag.SC_INTERFACE_VERSION.code());
    }

    public ScInterfaceVersion(Short tag, Byte value) {
        super(tag, value);
    }

    public ScInterfaceVersion(Tag tag, Byte value) {
        super(tag, value);
    }
}
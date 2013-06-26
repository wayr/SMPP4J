package org.wayr.smpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wayr.smpp.utils.IOStreamConverter;
import org.wayr.smpp.utils.MSBNumberToByteConverter;

/**
 *
 * @author paul
 */
public class GlobalConfiguration {

    //--
    protected static GlobalConfiguration configuration = null;
    //--
    protected IOStreamConverter bufferConverter = new IOStreamConverter(new MSBNumberToByteConverter());
    //--
    protected Logger logger = LoggerFactory.getLogger("org.wayr.smpp");

    protected GlobalConfiguration() {
    }

    public static GlobalConfiguration getInstance() {
        if (configuration == null) {
            configuration = new GlobalConfiguration();
        }
        return configuration;
    }

    public IOStreamConverter getBufferConverter() {
        return bufferConverter;
    }

    public void setBufferConverter(IOStreamConverter bufferConverter) {
        this.bufferConverter = bufferConverter;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}

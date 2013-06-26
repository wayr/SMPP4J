package org.wayr.smpp.listeners;

/**
 *
 * @author paul
 */
@Deprecated
public interface ConnectionBoundListener {

    public void onBindSuccess();

    public void onBindFailed();

    public void onConnectionClosed();

}

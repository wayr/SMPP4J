package org.wayr.smpp.trafficshapers;

import org.wayr.smpp.TrafficShaper;

/**
 *
 * @author paul
 */
public class NoTrafficShaper implements TrafficShaper {

    protected boolean started = false;

    public NoTrafficShaper() {
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void waitFor() {
    }

    @Override
    public void forceWakeup() {
    }

    @Override
    public void start() {
        this.started = true;
    }

    @Override
    public boolean isStarted() {
        return this.started;
    }

    @Override
    public void stop() {
        this.started = false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void increase() {
    }

    @Override
    public void decrease() {
    }
}

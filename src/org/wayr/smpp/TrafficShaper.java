package org.wayr.smpp;

/**
 *
 * @author paul
 */
public interface TrafficShaper {

    public void start();

    public boolean isStarted();

    public void stop();

    public void reset();

    public boolean isAvailable();

    public void increase();

    public void decrease();

    public void waitFor() throws InterruptedException;

    public void forceWakeup();
}

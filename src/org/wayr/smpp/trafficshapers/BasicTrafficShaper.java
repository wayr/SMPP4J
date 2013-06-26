package org.wayr.smpp.trafficshapers;

import org.wayr.smpp.TrafficShaper;


/**
 *
 * @author paul
 */
public class BasicTrafficShaper implements TrafficShaper{

    protected int maxElt;
    protected long unitTime;
    //-
    protected int nbUnit = 0;
    protected Long start;
    protected Long elapseTime;
    //-
    protected final Object lock = new Object();

    public BasicTrafficShaper(int nbUnitMax, long unitTime) {
        this.maxElt = nbUnitMax;
        this.unitTime = unitTime;
    }

    @Override
    public synchronized void start() {
        this.nbUnit = 0;
        this.start = this.now();
    }

    @Override
    public synchronized boolean isStarted() {
        return this.start != null;
    }

    @Override
    public synchronized void stop() {
        this.elapseTime = this.elapse();
        this.start = null;
    }

    public synchronized long elapse() {
        return this.start == null ? 0 : this.now() - this.start;
    }

    public synchronized int counter() {
        return this.nbUnit;
    }

    public synchronized long now() {
        return System.currentTimeMillis();
    }

    @Override
    public synchronized void increase() {
        if (!this.isStarted()) {
            this.start();
        }
        this.nbUnit++;
    }

    @Override
    public synchronized void decrease() {
        if (!this.isStarted()) {
            this.start();
        }
        this.nbUnit--;
    }

    @Override
    public synchronized void reset() {
        this.nbUnit = 0;
        this.start = null;
    }

    @Override
    public synchronized boolean isAvailable() {
        return this.getSleepTime() == 0l;
    }

    protected synchronized long getSleepTime() {
        if (!this.isStarted()) {
            this.start();
            return 0l;
        }

        if (this.nbUnit < this.maxElt) {
            return 0l;
        }

        /**
         * this.nbUnit >= this.maxElt
         */
        long sleepFor = this.unitTime - this.elapse();
        return sleepFor;
    }

    @Override
    public void waitFor() throws InterruptedException {
        long sleepFor = this.getSleepTime();

        if (sleepFor > 0) {
            synchronized (this.lock) {
                this.lock.wait(sleepFor);
            }
            this.reset();
        } else if (sleepFor < 0) {
            this.reset();
        }
    }

    @Override
    public void forceWakeup() {
        synchronized (this.lock) {
            this.lock.notifyAll();
        }
    }
}

package com.nicehash.clients.exchange.domain.market;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Sync handle -- impl detail.
 */
public class SyncHandle implements Closeable {

    private final Closeable closeable;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public SyncHandle(Closeable closeable) {
        this.closeable = closeable;
    }

    public Lock readLock() {
        return lock.readLock();
    }

    public Lock writeLock() {
        return lock.writeLock();
    }

    @Override
    public void close() throws IOException {
        closeable.close();
    }
}

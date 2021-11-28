package com.nikita.lock;

import com.nikita.helper.RandomHelper;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 1.1 - Create implementation via CAS (Atomic)
 * for synchronized, wait, notify, notifyAll
 */
public class Mutex {
    private final Set<Thread> waitingThreads;
    private final AtomicReference<Thread> lockThread;

    private static final int SLEEP_TIME_MS = 300;

    public Mutex() {
        waitingThreads = new HashSet<>();
        lockThread = new AtomicReference<>(null);
    }

    public void _lock() throws InterruptedException {
        /**
         * Obtaining a lock
         * If object is already locked - throw an exception
         */
        while (!this.lockThread.compareAndSet(null, Thread.currentThread())) {
            Thread.sleep(SLEEP_TIME_MS);
        }
    }

    public void _release() {
        lockThread.set(null);
    }

    public boolean _isLocked() {
        return lockThread.get() != null;
    }

    public void _wait() throws InterruptedException {
        waitingThreads.add(Thread.currentThread());
        this._release();

        while (waitingThreads.contains(Thread.currentThread())) {
            Thread.sleep(SLEEP_TIME_MS);
        }

        this._lock();
    }

    public void _notify() {
        if (waitingThreads.size() > 0) {
            waitingThreads.remove(RandomHelper.randomItemFromSet(waitingThreads));
        }
    }

    public void _notifyAll() {
        waitingThreads.clear();
    }
}

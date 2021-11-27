package com.nikita.lock;

import com.nikita.exception.ObjectLockedException;
import com.nikita.helper.RandomHelper;

import java.util.HashSet;
import java.util.Set;

public class Mutex {
    private Set<Thread> waitingThreads;
    private Set<Thread> lockThreads;

    private static final int WAIT_SLEEP_TIME_MS = 300;

    public Mutex() {
        waitingThreads = new HashSet<>();
        lockThreads = new HashSet<>();
    }

    public void _lock() throws ObjectLockedException {
        /**
         * Object is already locked - throw an exception
         */
        if (this._isLocked() == true) {
            throw new ObjectLockedException();
        }

        lockThreads.add(Thread.currentThread());
    }

    public void _release() {
        lockThreads.remove(Thread.currentThread());
    }

    public boolean _isLocked() {
        return lockThreads.size() > 0;
    }

    void _wait() throws InterruptedException {
        waitingThreads.add(Thread.currentThread());
        this._release();

        while (waitingThreads.contains(Thread.currentThread())) {
            Thread.sleep(WAIT_SLEEP_TIME_MS);
        }

        while (!lockThreads.contains(Thread.currentThread())) {
            try {
                this._lock();
            } catch (ObjectLockedException err) {
                continue;
            }
        }
    }

    void _notify() {
        waitingThreads.remove(RandomHelper.randomItemFromSet(waitingThreads));
    }

    void _notifyAll() {
        waitingThreads.clear();
    }
}

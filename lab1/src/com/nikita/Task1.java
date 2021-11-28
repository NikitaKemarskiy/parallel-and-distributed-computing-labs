package com.nikita;

import com.nikita.lock.Mutex;

public class Task1 implements Runnable {
    private Mutex mutex;

    public Task1(Mutex mutex) {
        this.mutex = mutex;
    }

    public static void main(String[] args) {
        Mutex mutex = new Mutex();

        Thread thread1 = new Thread(new Task1(mutex));
        Thread thread2 = new Thread(new Task1(mutex));
        Thread thread3 = new Thread(new Task1(mutex));

        thread1.start();
        thread2.start();
        thread3.start();
    }

    public void run() {
        try {
            mutex._lock();
        } catch (InterruptedException err) {
            throw new RuntimeException(err);
        }

        System.out.println(">>> 1: " + Thread.currentThread().getName());

        mutex._notify();

        System.out.println(">>> 2: " + Thread.currentThread().getName());

        try {
            mutex._wait();
        } catch (InterruptedException err) {
            throw new RuntimeException(err);
        }

        System.out.println(">>> 3: " + Thread.currentThread().getName());

        mutex._release();
    }
}

package com.nikita;

import com.nikita.queue.MichaelAndScottQueue;
import com.nikita.queue.NonBlockingQueue;

public class Task3 implements Runnable {
    private NonBlockingQueue<Integer> queue;

    public Task3(NonBlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    public static void main(String[] args) {
        NonBlockingQueue<Integer> queue = new MichaelAndScottQueue<>();

        Thread thread1 = new Thread(new Task3(queue));
        Thread thread2 = new Thread(new Task3(queue));
        Thread thread3 = new Thread(new Task3(queue));

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException err) {
            System.err.println(err);
        }

        System.out.println(queue.traverse());
    }

    public void run() {
        queue.add(1);
        queue.add(2);
        queue.add(3);
        queue.remove();
        queue.remove();
    }
}

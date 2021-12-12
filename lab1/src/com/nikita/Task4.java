package com.nikita;

import com.nikita.list.HarrisLinkedList;
import com.nikita.list.NonBlockingList;

public class Task4 implements Runnable {
    private NonBlockingList<Integer> list;

    public Task4(NonBlockingList<Integer> list) {
        this.list = list;
    }

    public static void main(String[] args) {
        NonBlockingList<Integer> list = new HarrisLinkedList<>();

        Thread thread1 = new Thread(new Task4(list));
        Thread thread2 = new Thread(new Task4(list));
        Thread thread3 = new Thread(new Task4(list));

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

        System.out.println(list.traverse());
    }

    public void run() {
        list.add(1);
        list.add(2);
        list.add(1, 3);
        list.remove(0);
    }
}

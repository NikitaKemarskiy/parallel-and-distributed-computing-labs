package com.nikita;

import com.nikita.list.SkipList;

public class Task2 implements Runnable {
    private SkipList<Integer> list;

    private static int getRandomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public Task2(SkipList<Integer> list) {
        this.list = list;
    }

    public static void main(String[] args) {
        SkipList<Integer> list = new SkipList<>();

        Thread thread1 = new Thread(new Task2(list));
        Thread thread2 = new Thread(new Task2(list));
        Thread thread3 = new Thread(new Task2(list));

        thread1.start();
        // thread2.start();
        // thread3.start();

        try {
            thread1.join();
            // thread2.join();
            // thread3.join();
        } catch (InterruptedException err) {
            System.err.println(err);
        }

        System.out.println(">>> List contains 0: " + list.contains(0));
        System.out.println(">>> List contains 1: " + list.contains(1));
        System.out.println(">>> List contains 2: " + list.contains(2));
        System.out.println(">>> List contains 3: " + list.contains(3));
        System.out.println(">>> List contains 4: " + list.contains(4));

        System.out.println(list.traverse());
    }

    public void run() {
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(getRandomInt(1, 4));
    }
}

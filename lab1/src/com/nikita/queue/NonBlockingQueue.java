package com.nikita.queue;

public interface NonBlockingQueue<T> {
    void add(T elem);
    T remove();
    String traverse();
}

package com.nikita.list;

public interface NonBlockingQueue<T> {
    void add(T elem);
    T remove();
}

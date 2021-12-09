package com.nikita.list;

public interface LinkedList<T> {
    void add(T elem);
    void add(int index, T elem);
    void remove(int index);
    T get(int index);
}

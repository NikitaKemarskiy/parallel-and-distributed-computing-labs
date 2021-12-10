package com.nikita.list;

import java.util.concurrent.atomic.AtomicReference;

public class HarrisLinkedListNode<T> {
    private T value;
    private AtomicReference<HarrisLinkedListNode<T>> next;

    public HarrisLinkedListNode(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public AtomicReference<HarrisLinkedListNode<T>> getNext() {
        return next;
    }

    public void setNext(AtomicReference<HarrisLinkedListNode<T>> next) {
        this.next = next;
    }
}

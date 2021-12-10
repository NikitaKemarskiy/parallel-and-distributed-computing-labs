package com.nikita.list;

import java.util.concurrent.atomic.AtomicReference;

public class MichaelAndScottQueue<T> implements NonBlockingQueue<T> {
    private volatile AtomicReference<Node<T>> head;
    private volatile AtomicReference<Node<T>> tail;

    private static class Node<T> {
        private T value;
        private AtomicReference<Node<T>> next;

        public Node(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public AtomicReference<Node<T>> getNext() {
            return next;
        }

        public void setNext(AtomicReference<Node<T>> next) {
            this.next = next;
        }
    }

    public MichaelAndScottQueue() {
        Node dummyNode = new Node(null);
        head = new AtomicReference<>(dummyNode);
        tail = new AtomicReference<>(dummyNode);
    }

    public void add(T elem) {
        //
    }

    public T remove(int index) {
        while (true) {
            AtomicReference<Node<T>> node = head.get().getNext();

            if (node == null) {
                return null;
            }

            if (head.get().getNext().compareAndSet(node.get(), node.get().getNext().get())) {
                return node.get().getValue();
            }
        }
    }
}

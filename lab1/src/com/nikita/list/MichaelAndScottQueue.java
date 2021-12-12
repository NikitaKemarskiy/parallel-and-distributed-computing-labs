package com.nikita.list;

import java.util.concurrent.atomic.AtomicReference;

public class MichaelAndScottQueue<T> implements NonBlockingQueue<T> {
    private volatile Node<T> head;
    private volatile Node<T> tail;

    private static class Node<T> {
        private T value;
        private AtomicReference<Node<T>> next;

        public Node(T value) {
            this.value = value;
            this.next = new AtomicReference<>(null);
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getNext() {
            return next.get();
        }

        public boolean compareAndSetNext(Node<T> expected, Node<T> next) {
            return this.next.compareAndSet(expected, next);
        }
    }

    public MichaelAndScottQueue() {
        Node dummyNode = new Node(null);
        head = dummyNode;
        tail = dummyNode;
    }

    @Override
    public void add(T elem) {
        Node<T> node = new Node(elem);

        while (true) {
            if (tail.compareAndSetNext(null, node)) {
                tail = node;
                break;
            } else {
                tail = tail.getNext();
            }
        }
    }

    @Override
    public T remove() {
        Node<T> node;

        while (true) {
            node = head.getNext();

            if (node == null) {
                return null;
            }

            if (head == tail) {
                tail = node;
            } else if (head.compareAndSetNext(node, node.getNext())) {
                return node.getValue();
            }
        }
    }
}

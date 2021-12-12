package com.nikita.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
                Node nextNode = tail.getNext();

                if (nextNode != null) {
                    tail = nextNode;
                }
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

    @Override
    public String traverse() {
        Node<T> currNode = head.getNext();
        List<String> out = new LinkedList<>();

        while (currNode != null) {
            out.add(currNode.getValue().toString());
            currNode = currNode.getNext();
        }

        return out.stream().collect(Collectors.joining(","));
    }
}

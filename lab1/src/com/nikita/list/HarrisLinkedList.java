package com.nikita.list;

import com.nikita.queue.MichaelAndScottQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * It's a naive implementation of a Harris's solution.
 * So there are still problems like:
 * 1. Concurrent insert and delete
 * 2. Concurrent deletions
 * @param <T>
 */
public class HarrisLinkedList<T> implements NonBlockingList<T> {
    private volatile AtomicReference<Node<T>> head;
    private volatile int size;

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

        public void setNext(Node<T> next) {
            this.next.set(next);
        }

        public boolean compareAndSetNext(Node<T> expected, Node<T> next) {
            return this.next.compareAndSet(expected, next);
        }
    }

    private Node<T> getNode(int index) {
        Node<T> currNode = head.get();
        int currIndex = 0;

        while (
            currIndex < index &&
            currNode != null
        ) {
            currNode = currNode.getNext();
            currIndex++;
        }

        return currIndex == index ? currNode : null;
    }

    public HarrisLinkedList() {
        head = new AtomicReference<>(null);
        size = 0;
    }

    /**
     * Add element to the ending of the list
     * @param elem - Element to add
     */
    @Override
    public void add(T elem) {
        add(-1, elem);
    }

    /**
     * @param index - Index to add an element (if index == -1 - add element to the ending of the list)
     * @param elem - Element to add
     */
    @Override
    public void add(int index, T elem) {
        Node<T> node = new Node<>(elem);
        Node<T> prevNode;
        Node<T> nextNode;

        while (true) {
            /**
             * Head is empty - add an element as head
             */
            if (head.get() == null) {
                if (head.compareAndSet(null, node)) {
                    size++;
                    break;
                }
            }
            /**
             * Head isn't empty - add an element by index
             */
            else if (head.get() != null) {
                /**
                 * Check whether index == -1
                 * If yes then insert element
                 * at the ending of the list
                 */
                prevNode = getNode(index == -1 ? size - 1 : index - 1);

                if (prevNode == null) {
                    continue;
                }

                nextNode = prevNode.getNext();
                node.setNext(nextNode);

                if (prevNode.compareAndSetNext(nextNode, node)) {
                    size++;
                    break;
                }
            }
        }
    }

    @Override
    public void remove(int index) {
        Node<T> node;
        Node<T> prevNode;

        while (true) {
            node = getNode(index);

            /**
             * Remove the first element when
             * there's only one element in list.
             * Set null as head
             */
            if (
                index == 0 &&
                head.get().getNext() == null
            ) {
                if (head.compareAndSet(node, null)) {
                    break;
                }
            }
            /**
             * Remove the first element when
             * there're multiple elements in list.
             * Set the second element as head
             */
            else if (
                index == 0 &&
                head.get().getNext() != null
            ) {
                if (head.compareAndSet(node, node.getNext())) {
                    break;
                }
            }
            /**
             * Remove not the first element
             */
            else {
                prevNode = getNode(index - 1);

                if (prevNode.compareAndSetNext(node, node.getNext())) {
                    break;
                }
            }
        }
    }

    @Override
    public T get(int index) {
        return getNode(index).getValue();
    }

    @Override
    public String traverse() {
        Node<T> currNode = head.get();
        List<String> out = new LinkedList<>();

        while (currNode != null) {
            out.add(currNode.getValue().toString());
            currNode = currNode.getNext();
        }

        return out.stream().collect(Collectors.joining(","));
    }
}

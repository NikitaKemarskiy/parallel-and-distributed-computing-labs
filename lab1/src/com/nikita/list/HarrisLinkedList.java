package com.nikita.list;

import java.util.concurrent.atomic.AtomicReference;

public class HarrisLinkedList<T> implements NonBlockingList<T> {
    private volatile AtomicReference<HarrisLinkedListNode<T>> head;
    private volatile int size;

    public HarrisLinkedList() {
        head = new AtomicReference<>(null);
        size = 0;
    }

    private AtomicReference<HarrisLinkedListNode<T>> getNodeAtomicReference(int index) {
        AtomicReference<HarrisLinkedListNode<T>> currNodeAtomicReference = head;
        int currIndex = 0;

        while (currIndex < index && currNodeAtomicReference.get().getNext() != null) {
            currNodeAtomicReference = currNodeAtomicReference.get().getNext();
            currIndex++;
        }

        return currIndex == index ? currNodeAtomicReference : null;
    }

    /**
     * Add element to the ending of the list
     * @param elem - Element to add
     */
    public void add(T elem) {
        add(-1, elem);
    }

    /**
     * @param index - Index to add an element (if index == -1 - add element to the ending of the list)
     * @param elem - Element to add
     */
    public void add(int index, T elem) {
        HarrisLinkedListNode<T> node = new HarrisLinkedListNode<>(elem);
        AtomicReference<HarrisLinkedListNode<T>> prevNode;

        while (true) {
            /**
             * Head is empty - add an element as head
             */
            if (head.get() == null && head.compareAndSet(null, node)) {
                size++;
                break;
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
                prevNode = getNodeAtomicReference(index == -1 ? size - 1 : index - 1);

                if (prevNode == null) {
                    continue;
                }

                node.setNext(prevNode.get().getNext());

                if (prevNode.get().getNext().compareAndSet(prevNode.get().getNext().get(), node)) {
                    size++;
                    break;
                }
            }
        }
    }

    public void remove(int index) {
        AtomicReference<HarrisLinkedListNode<T>> node;
        AtomicReference<HarrisLinkedListNode<T>> prevNode;

        while (true) {
            node = getNodeAtomicReference(index);

            /**
             * Remove the first element when
             * there's only one element in list.
             * Set null as head
             */
            if (index == 0 && head.get().getNext() == null) {
                head.compareAndSet(node.get(), null);
            }
            /**
             * Remove the first element when
             * there're multiple elements in list.
             * Set the second element as head
             */
            else if (index == 0 && head.get().getNext() != null) {
                head.compareAndSet(node.get(), node.get().getNext().get());
            }
            /**
             * Remove any element except the first
             */
            else {
                prevNode = getNodeAtomicReference(index - 1);

                prevNode.get().getNext().compareAndSet(node.get(), node.get().getNext().get());
            }
        }
    }

    public T get(int index) {
        return getNodeAtomicReference(index).get().getValue();
    }
}

package com.nikita.list;

public class HarrisLinkedList<T> implements LinkedList<T> {
    private HarrisLinkedListNode head;

    private HarrisLinkedListNode getTail() {
        HarrisLinkedListNode curr = head;

        while (curr != null && curr.getNext() != null) {
            curr = curr.getNext();
        }

        return curr;
    }

    public void add(T elem) {
        HarrisLinkedListNode node = new HarrisLinkedListNode(elem);

    }

    public void add(int index, T elem) {}

    public void remove(int index) {}

    public T get(int index) {}
}

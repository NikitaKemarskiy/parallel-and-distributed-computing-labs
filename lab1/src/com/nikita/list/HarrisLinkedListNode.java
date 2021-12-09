package com.nikita.list;

public class HarrisLinkedListNode<T> {
    private T elem;
    private HarrisLinkedListNode next;

    public HarrisLinkedListNode(T elem) {
        this.elem = elem;
    }

    public HarrisLinkedListNode(T elem, HarrisLinkedListNode next) {
        this.elem = elem;
        this.next = next;
    }

    public HarrisLinkedListNode getNext() {
        return next;
    }

    public void setNext(HarrisLinkedListNode next) {
        this.next = next;
    }
}

package com.nikita.list;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SkipList<T> {
    private int maxHeight;
    private Node<T> headTop;

    private static int DEFAULT_MAX_HEIGHT = 32;
    private static class Node<T> {
        private T value;
        private Node<T> rightNode;
        private Node<T> lowerNode;

        public Node(T value) {
            this.value = value;
            this.rightNode = null;
            this.lowerNode = null;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getRightNode() {
            return rightNode;
        }

        public void setRightNode(Node<T> rightNode) {
            this.rightNode = rightNode;
        }

        public Node<T> getLowerNode() {
            return lowerNode;
        }

        public void setLowerNode(Node<T> lowerNode) {
            this.lowerNode = lowerNode;
        }
    }

    private Node<T> getHeadBottom() {
        Node<T> currNode = headTop;

        while (currNode.getLowerNode() != null) {
            currNode = currNode.getLowerNode();
        }

        return currNode;
    }

    public SkipList() {
        Node<T> currNode = new Node(null);

        maxHeight = DEFAULT_MAX_HEIGHT;
        headTop = currNode;

        for (int i = 1; i < maxHeight; i++) {
            Node<T> nextNode = new Node(null);
            currNode.setLowerNode(nextNode);

            currNode = nextNode;
        }
    }

    public void add(T elem) {

    }

    public void remove(int index) {

    }

    public String traverse() {
        Node<T> currNode = getHeadBottom().getRightNode();
        List<String> out = new LinkedList<>();

        while (currNode != null) {
            out.add(currNode.getValue().toString());
            currNode = currNode.getRightNode();
        }

        return out.stream().collect(Collectors.joining(","));
    }
}

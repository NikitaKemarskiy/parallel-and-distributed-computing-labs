package com.nikita.list;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class SkipList<T extends Comparable<T>> {
    private int maxHeight;
    private Node<T> headTop;

    private static int DEFAULT_MAX_HEIGHT = 32;
    private static class Node<T> {
        private T value;
        private AtomicReference<Node<T>> rightNode;
        private AtomicReference<Node<T>> lowerNode;
        private boolean toBeDeleted;

        public Node(T value) {
            this.value = value;
            this.rightNode = new AtomicReference<>(null);
            this.lowerNode = new AtomicReference<>(null);
            this.toBeDeleted = false;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getRightNode() {
            return rightNode.get();
        }

        public void setRightNode(Node<T> rightNode) {
            this.rightNode.set(rightNode);
        }

        public Node<T> getLowerNode() {
            return lowerNode.get();
        }

        public void setLowerNode(Node<T> lowerNode) {
            this.lowerNode.set(lowerNode);
        }

        public boolean isToBeDeleted() {
            return toBeDeleted;
        }

        public void setToBeDeleted(boolean toBeDeleted) {
            this.toBeDeleted = toBeDeleted;
        }

        public boolean compareAndSetRightNode(Node<T> expected, Node<T> next) {
            return !toBeDeleted && this.rightNode.compareAndSet(expected, next);
        }

        public boolean compareAndSetLowerNode(Node<T> expected, Node<T> next) {
            return this.lowerNode.compareAndSet(expected, next);
        }
    }

    private Node<T> getHeadBottomNode() {
        return getHeadNodeByIndex(0);
    }

    private Node<T> getHeadNodeByIndex(int index) {
        Node<T> currNode = headTop;

        for (int i = maxHeight - 1; i > index; i--) {
            currNode = currNode.getLowerNode();
        }

        return currNode;
    }

    private int getNodeHeight() {
        Random random = new Random();
        int level = 1;

        while (level < maxHeight && Math.abs(random.nextInt()) % 2 == 0) {
            level++;
        }

        return level;
    }

    public SkipList() {
        Node<T> currNode = new Node(null);

        maxHeight = DEFAULT_MAX_HEIGHT;
        headTop = currNode;

        for (int i = 1; i < maxHeight; i++) {
            Node<T> nextNode = new Node(null);
            currNode.compareAndSetLowerNode(null, nextNode);

            currNode = nextNode;
        }
    }

    public void add(T elem) {
        int height = getNodeHeight();

        Node<T> currNode = getHeadNodeByIndex(height - 1);
        Node<T> prevAddedNode = null;

        while (currNode != null) {
            Node<T> rightNode = currNode.getRightNode();

            /**
             * Right node is less than a node to add.
             * Proceed to the right node.
             */
            if (
                rightNode != null &&
                rightNode.getValue().compareTo(elem) == -1
            ) {
                currNode = rightNode;
            } else {
                Node<T> node = new Node(elem);

                node.setRightNode(rightNode);

                if (currNode.compareAndSetRightNode(rightNode, node)) {
                    currNode = currNode.getLowerNode();

                    /**
                     * Set added node as a lower node for
                     * previously added node.
                     */
                    if (prevAddedNode != null) {
                        prevAddedNode.setLowerNode(node);
                    }

                    prevAddedNode = node;
                }
            }
        }
    }

    public void remove(T elem) {
        Node<T> currNode = getHeadBottomNode();

        while (currNode != null) {
            Node<T> rightNode = currNode.getRightNode();

            /**
             * Right node is less than a node to add.
             * Proceed to the right node.
             */
            if (
                rightNode != null &&
                rightNode.getValue().compareTo(elem) == -1
            ) {
                currNode = rightNode;
            } else if (
                rightNode != null &&
                rightNode.getValue().compareTo(elem) == 0
            ) {
                rightNode.setToBeDeleted(true);

                if (currNode.compareAndSetRightNode(rightNode, rightNode.getRightNode())) {
                    currNode = currNode.getLowerNode();
                } else {
                    rightNode.setToBeDeleted(false);
                }
            } else {
                currNode = currNode.getLowerNode();
            }
        }
    }

    public boolean contains(T elem) {
        Node<T> currNode = getHeadBottomNode().getRightNode();

        while (currNode != null && !currNode.getValue().equals(elem)) {
            Node<T> rightNode = currNode.getRightNode();

            /**
             * Right node is less than a node to add.
             * Proceed to the right node.
             */
            if (
                rightNode != null &&
                rightNode.getValue().compareTo(elem) != 1
            ) {
                currNode = rightNode;
            } else {
                currNode = currNode.getLowerNode();
            }
        }

        return currNode != null;
    }

    public String traverse() {
        Node<T> currNode = getHeadBottomNode().getRightNode();
        List<String> out = new LinkedList<>();

        while (currNode != null) {
            out.add(currNode.getValue().toString());
            currNode = currNode.getRightNode();
        }

        return out.stream().collect(Collectors.joining(","));
    }
}

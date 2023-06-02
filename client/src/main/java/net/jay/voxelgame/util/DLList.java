package net.jay.voxelgame.util;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class DLList<E> implements Iterable<E> {
    private final Node<E> head = new Node<>(null);
    private final Node<E> tail = new Node<>(null);
    private int length = 0;

    public DLList() {
        head.setNext(tail);
        tail.setPrev(head);
    }

    public DLList(DLList<E> src) {
        this();
        this.length = 0;
        for(E el : src) {
            add(el);
        }
    }

    public void add(E element) {
        Node<E> node = new Node<>(element);

        node.setPrev(tail.prev());
        node.setNext(tail);

        tail.prev().setNext(node);
        tail.setPrev(node);
        length++;
    }

    public void insertAtBeginning(E element) {
        Node<E> node = new Node<>(element);

        node.setPrev(head);
        node.setNext(head.next());
        head.next().setPrev(node);
        head.setNext(node);
        length++;
    }

    public E get(int index) {
        if(!inBounds(index))
            return null;
        return getNode(index).get();
    }

    public Node<E> getNode(int index) {
        boundsCheck(index);

        int mid = length / 2;

        Node<E> node;
        if(index <= mid) {
            node = head.next();
            for(int i = 0; i < index; i++) {
                node = node.next();
            }
        } else {
            node = tail.prev();
            for(int i = length - 1; i > index; i--) {
                node = node.prev();
            }
        }

        return node;
    }

    public boolean contains(E element) {
        for(E el : this) {
            if(element == el) return true;
        }

        return false;
    }

    public void swap(int firstIndex, int secondIndex) {
        Node<E> first = getNode(firstIndex);
        Node<E> second = getNode(secondIndex);

        // Simply just swap the data
        E orig = first.get();
        first.set(second.get());
        second.set(orig);
    }

    public void remove(int index) {
        if(!inBounds(index))
            return;
        Node<E> node = getNode(index);
        Node<E> prev = node.prev();
        Node<E> next = node.next();

        prev.setNext(next);
        next.setPrev(prev);

        node.setPrev(null);
        node.setNext(null);

        length--;
    }

    public void shuffle() {
        ThreadLocalRandom rand = ThreadLocalRandom.current();

        for(int i = 0; i < length; i++) {
            int swapPos = rand.nextInt(0, length);

            swap(swapPos, i);
        }
    }

    public boolean inBounds(int index) {
        return index >= 0 && index < length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public int size() {
        return length;
    }

    public void print() {
        System.out.println(this);
    }

    private void boundsCheck(int index) {
        if(index < 0 || index > length - 1) throw new IndexOutOfBoundsException();
    }

    public static <E> DLList<E> copy(DLList<E> src) {
        return new DLList<>(src);
    }

    @Override
    public String toString() {
        if(isEmpty()) return "[]";

        StringBuilder sb = new StringBuilder("[");

        int index = 0;
        Node<E> currentNode = head.next();
        while(currentNode.next() != null) {
            sb.append("\n    ").append(index).append(": ").append(currentNode.get()).append(",");
            currentNode = currentNode.next();
            index++;
        }
        // Remove last comma
        sb.replace(sb.lastIndexOf(","), sb.lastIndexOf(",") + 1, "");
        sb.append("\n]");

        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new DLListIterator();
    }

    // More efficient implementation possible by caching the last retrieved node
    public class DLListIterator implements Iterator<E> {
        private Node<E> current = head.next();

        @Override
        public boolean hasNext() {
            return current != tail;
        }

        @Override
        public E next() {
            E data = current.get();
            current = current.next();
            return data;
        }

        @Override
        public void remove() {
            Iterator.super.remove();
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Iterator.super.forEachRemaining(action);
        }
    }

    private static class Node<E> {
        private E data;
        private Node<E> prev = null;
        private Node<E> next = null;

        public Node(E data) {
            this.data = data;
        }

        public E get() {
            return data;
        }

        public void set(E data) {
            this.data = data;
        }

        public Node<E> prev() {
            return prev;
        }

        public Node<E> next() {
            return next;
        }

        public void setPrev(Node<E> prev) {
            this.prev = prev;
        }

        public void setNext(Node<E> next) {
            this.next = next;
        }
    }

}

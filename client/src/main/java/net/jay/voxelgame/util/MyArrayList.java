package net.jay.voxelgame.util;

import java.util.Iterator;

public class MyArrayList<E> implements Iterable<E> {
    private Object[] data;
    private int increment = 32;
    private int size;

    public MyArrayList() {
        this.data = new Object[32];
    }

    public MyArrayList(int customIncrement) {
        this.data = new Object[32];
        this.increment = customIncrement;
    }

    public MyArrayList(int startingSize, int customIncrement) {
        this.data = new Object[startingSize];
        this.increment = customIncrement;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if(index < 0 || index >= size)
            return null;

        return (E)data[index];
    }

    public void add(E element) {
        if(size == data.length)
            expandArray();

        data[size] = element;
        size++;
    }

    public void expandArray() {
        Object[] newArray = new Object[size + increment];

        if(size >= 0)
            System.arraycopy(data, 0, newArray, 0, size);

        data = newArray;
    }

    public void remove(int index) {
        if(index < 0 || index >= size)
            return;
        Object[] newArray = new Object[data.length - 1];

        System.arraycopy(data, 0, newArray, 0, index);
        size--;
        System.arraycopy(data, index + 1, newArray, index, size - index);

        data = newArray;
    }

    public void remove(E element) {
        for(int i = 0; i < data.length; i++) {
            if(data[i].equals(element)) {
                remove(i);
                break;
            }
        }
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int nextIndex = 0;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            public E next() {
                E element = get(nextIndex);
                nextIndex++;
                return element;
            }
        };
    }
}

/*
* Created at 17:15 on 04/02/2017
*/
package com.example.util;

import java.util.AbstractList;
import java.util.Collection;

/**
 * @author zzhao
 */
public class CircularArrayList<E> extends AbstractList<E> {

    public static final int DEFAULT_INIT_CAPACITY = 10;

    private Object[] elements;

    private int size;

    private int head = 0;

    public CircularArrayList() {
        this(DEFAULT_INIT_CAPACITY);
    }

    public CircularArrayList(int initCapacity) {
        this.elements = new Object[initCapacity];
        this.size = 0;
    }

    public CircularArrayList(Collection<? extends E> c) {
        this.elements = c.toArray();
        this.size = c.size();
    }

    @SuppressWarnings("all")
    @Override
    public E get(int index) {
        checkIndexRange(index);
        final int realIndex = calculateRealIndex(index);
        return (E) this.elements[realIndex];
    }

    private int calculateRealIndex(int index) {
        return (this.head + index) % this.elements.length;
    }

    private void checkIndexRange(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(int index, E e) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        }

        ensureCapacity();
        int realIndex = calculateRealIndex(index);
        if (index != this.size) {
            if (index == 0) {
                moveHead(-1);
                realIndex = calculateRealIndex(index);
            } else if (realIndex > this.head && this.head != 0) {
                System.arraycopy(this.elements, this.head, this.elements, this.head - 1, index);
                moveHead(-1);
                realIndex = calculateRealIndex(index);
            } else {
                System.arraycopy(this.elements, realIndex, this.elements, realIndex + 1, this.size - index);
            }
        }
        this.elements[realIndex] = e;
        this.size++;
        this.modCount++;
    }

    private void moveHead(int steps) {
        this.head += steps;
        this.head %= this.elements.length;
        if (this.head < 0) {
            this.head += this.elements.length;
        }
    }

    private void ensureCapacity() {
        if (this.size == this.elements.length) {
            final Object[] newElements = new Object[(this.elements.length + 1) * 3 / 2];
            final int elementCount = this.elements.length - this.head;
            System.arraycopy(this.elements, this.head, newElements, 0, elementCount);
            System.arraycopy(this.elements, 0, newElements, elementCount, this.head);
            this.elements = newElements;
            this.head = 0;
        }
    }

    @SuppressWarnings("all")
    @Override
    public E set(int index, E element) {
        checkIndexRange(index);
        final int realIndex = calculateRealIndex(index);
        final E ret = (E) this.elements[realIndex];
        this.elements[realIndex] = element;
        return ret;
    }

    @SuppressWarnings("all")
    @Override
    public E remove(int index) {
        checkIndexRange(index);
        int realIndex = calculateRealIndex(index);
        final E ret = (E) this.elements[realIndex];

        if (index == this.size - 1) {
            this.elements[realIndex] = null;
        } else if (index == 0) {
            this.elements[realIndex] = null;
            moveHead(1);
        } else if (realIndex > this.head) {
            System.arraycopy(this.elements, this.head, this.elements, this.head + 1, index);
            this.elements[this.head] = null;
            moveHead(1);
        } else {
            System.arraycopy(this.elements, realIndex + 1, this.elements, realIndex, this.size - index);
            this.elements[calculateRealIndex(this.size - 1)] = null;
        }

        this.size--;
        this.modCount++;
        return ret;
    }
}

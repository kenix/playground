/*
* Created at 17:15 on 04/02/2017
*/
package com.example.util;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author zzhao
 */
public class MyArrayList<E> extends AbstractList<E> {

    public static final int DEFAULT_INIT_CAPACITY = 10;

    private Object[] elements;

    private int size;

    public MyArrayList() {
        this(DEFAULT_INIT_CAPACITY);
    }

    public MyArrayList(int initCapacity) {
        this.elements = new Object[initCapacity];
        this.size = 0;
    }

    public MyArrayList(Collection<? extends E> c) {
        this.elements = c.toArray();
        this.size = c.size();
    }

    @SuppressWarnings("all")
    @Override
    public E get(int index) {
        checkIndexRange(index);
        return (E) this.elements[index];
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
        if (index != this.size) {
            System.arraycopy(this.elements, index, this.elements, index + 1, this.size - index);
        }
        this.elements[index] = e;
        this.size++;
        this.modCount++;
    }

    private void ensureCapacity() {
        if (this.size == this.elements.length) {
            this.elements = Arrays.copyOf(this.elements, (this.elements.length + 1) * 3 / 2);
        }
    }

    @SuppressWarnings("all")
    @Override
    public E set(int index, E element) {
        checkIndexRange(index);
        final E ret = (E) this.elements[index];
        this.elements[index] = element;
        return ret;
    }

    @SuppressWarnings("all")
    @Override
    public E remove(int index) {
        checkIndexRange(index);
        final E ret = (E) this.elements[index];
        if (index != this.size - 1) {
            System.arraycopy(this.elements, index + 1, this.elements, index, this.size - index - 1);
        }
        this.elements[this.size - 1] = null;
        this.size--;
        this.modCount++;
        return ret;
    }
}

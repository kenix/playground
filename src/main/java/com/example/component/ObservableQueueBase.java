/*
* Created at 23:04 on 18/11/15
*/
package com.example.component;

import rx.Observable;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * RxJava does not enforce any threading by default. To consume items on a thread other than the producing thread
 * use following pattern:
 * <pre>
 *     ObservableQueue
 *      .observe()
 *      .observeOn(Schedulers.newThread())
 *      .forEach()
 * </pre>
 *
 * @author zzhao
 */
public abstract class ObservableQueueBase<T> implements BlockingQueue<T>, Closeable {

    public abstract Observable<T> observe();

    @Override
    public abstract boolean offer(T t);

    @Override
    public abstract void close() throws IOException;

    @Override
    public boolean add(T t) {
        return offer(t);
    }

    private T noSuchElement() {
        throw new NoSuchElementException();
    }

    @Override
    public T remove() {
        return noSuchElement();
    }

    @Override
    public T poll() {
        return null;
    }

    @Override
    public T element() {
        return noSuchElement();
    }

    @Override
    public T peek() {
        return null;
    }

    @Override
    public void put(T t) throws InterruptedException {
        offer(t);
    }

    @Override
    public boolean offer(T t, long timeout, TimeUnit unit) throws InterruptedException {
        return offer(t);
    }

    @Override
    public T take() throws InterruptedException {
        throw new UnsupportedOperationException("use observe instead");
    }

    @Override
    public T poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        c.forEach(this::offer);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return a;
    }

    @Override
    public int drainTo(Collection<? super T> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super T> c, int maxElements) {
        return 0;
    }
}

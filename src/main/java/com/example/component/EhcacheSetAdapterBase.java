/*
* vwd KL
* Created by zzhao on 11/20/15 6:09 PM
*/
package com.example.component;

import java.util.Collection;
import java.util.Set;

import net.sf.ehcache.Ehcache;

/**
 * @author zzhao
 */
public abstract class EhcacheSetAdapterBase<T> implements Set<T> {

    protected final Ehcache target;

    protected EhcacheSetAdapterBase(Ehcache target) {
        this.target = target;
    }

    @Override
    public int size() {
        return this.target.getSize();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }


    @Override
    public boolean add(T t) {
        throw new UnsupportedOperationException("not implemented yet");
    }


    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public void clear() {
        this.target.removeAll();
    }
}

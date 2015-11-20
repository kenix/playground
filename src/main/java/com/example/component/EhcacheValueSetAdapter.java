/*
* vwd KL
* Created by zzhao on 11/20/15 6:20 PM
*/
package com.example.component;

import java.util.Collection;
import java.util.Iterator;

import net.sf.ehcache.Ehcache;

/**
 * @author zzhao
 */
public class EhcacheValueSetAdapter<T> extends EhcacheSetAdapterBase<T> {

    public EhcacheValueSetAdapter(Ehcache target) {
        super(target);
    }

    @Override
    public boolean contains(Object o) {
        return this.target.isValueInCache(o);
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("not implemented yet");
    }
}

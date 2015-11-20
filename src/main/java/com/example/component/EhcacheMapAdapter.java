/*
* vwd KL
* Created by zzhao on 11/20/15 4:07 PM
*/
package com.example.component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * Cheap map adapter based on ehcache to simplify cache operations and support generics.
 * <p>
 * Expensive operations are not supported, including keySet, values, EntrySet and value containment.
 * </p>
 * @author zzhao
 */
public class EhcacheMapAdapter<K, V> implements Map<K, V> {

    private final Ehcache target;

    public EhcacheMapAdapter(Ehcache target) {
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
    public boolean containsKey(Object key) {
        return this.target.get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("not supported");
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        return containsKey(key) ? (V) this.target.get(key).getObjectValue() : null;
    }

    @Override
    public V put(K key, V value) {
        final V previousVal = get(key);
        this.target.put(new Element(key, value));
        return previousVal;
    }

    @Override
    public V remove(Object key) {
        final V previousVal = get(key);
        this.target.remove(key);
        return previousVal;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        this.target.removeAll();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("not supported");
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("not supported");
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("not supported");
    }
}

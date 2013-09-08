package gui.customElements.accordion;

import java.util.Map;

public final class ListEntry<K, V> implements Map.Entry<K, V> {
    private final K _key;
    private V _value;

    public ListEntry(K key, V value) {
        this._key = key;
        this._value = value;
    }

    @Override
    public K getKey() {
        return _key;
    }

    @Override
    public V getValue() {
        return _value;
    }

    @Override
    public V setValue(V value) {
        V old = this._value;
        this._value = value;
        return old;
    }
}
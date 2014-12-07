package com.appkernel.json;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;


/**
 * Type-Filter Iterator implementation for JSONObject
 * 
 * @author vadim
 *
 * @param <T>
 */
public class JSONObjectIterator<T> implements Iterable<Map.Entry<String, T>>, Iterator<Map.Entry<String, T>> {

	private final JSONObject mJsonObject;
	private final Class<T>   mClazz;
	
	private final Iterator<String> mKeysIterator;
	
	private String mKeysIteratorCurrent = null;
	
	
	@SuppressWarnings("unchecked")
	public JSONObjectIterator(JSONObject jsonObject, Class<T> clazz) {
		mJsonObject = jsonObject;
		mClazz      = clazz;
		
		mKeysIterator = jsonObject == null ? null: mJsonObject.keys();
		moveIteratorToNextMatchingPosition();
	}

	@Override
	public boolean hasNext() {
		return mKeysIteratorCurrent != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Entry<String, T> next() {
		if (mKeysIterator == null)
			return null;
		
		Entry<String, T> res = new SimpleImmutableEntry<String, T>(mKeysIteratorCurrent,  (T) mJsonObject.opt(mKeysIteratorCurrent) );
		moveIteratorToNextMatchingPosition();
		
		return res;
	}
	

	@Override
	public void remove() {
		throw new UnsupportedOperationException("JSONObjectIterator is readonly");
	}

	@Override
	public Iterator<Entry<String, T>> iterator() {
		return this;
	}
	
	
	private void moveIteratorToNextMatchingPosition() {
		if (mKeysIterator == null)
			return;
		
		while ( mKeysIterator.hasNext() ) {
			mKeysIteratorCurrent = mKeysIterator.next();
			Object o = mJsonObject.opt(mKeysIteratorCurrent);
			if (o != null && mClazz.isInstance(o))
				return;
		}
		mKeysIteratorCurrent = null;
	}
	
	
	
    /**
     * This is the clone of http://developer.android.com/reference/java/util/AbstractMap.SimpleImmutableEntry.html
     * Which is not exists on Android before version 9
     * 
     * An immutable key-value mapping. Despite the name, this class is non-final
     * and its subclasses may be mutable.
     *
     * @since 1.6
     */
    public static class SimpleImmutableEntry<K, V> implements Map.Entry<K, V>, Serializable {
        private static final long serialVersionUID = 7138329143949025153L;

        private final K key;
        private final V value;

        public SimpleImmutableEntry(K theKey, V theValue) {
            key = theKey;
            value = theValue;
        }

        /**
         * Constructs an instance with the key and value of {@code copyFrom}.
         */
        public SimpleImmutableEntry(Map.Entry<? extends K, ? extends V> copyFrom) {
            key = copyFrom.getKey();
            value = copyFrom.getValue();
        }

        @Override public K getKey() {
            return key;
        }

        @Override public V getValue() {
            return value;
        }

        /**
         * This base implementation throws {@code UnsupportedOperationException}
         * always.
         */
        @Override public V setValue(V object) {
            throw new UnsupportedOperationException();
        }

        @Override public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) object;
                return (key == null ? entry.getKey() == null : key.equals(entry
                        .getKey()))
                        && (value == null ? entry.getValue() == null : value
                                .equals(entry.getValue()));
            }
            return false;
        }

        @Override public int hashCode() {
            return (key == null ? 0 : key.hashCode())
                    ^ (value == null ? 0 : value.hashCode());
        }

        @Override public String toString() {
            return key + "=" + value;
        }
    }
}

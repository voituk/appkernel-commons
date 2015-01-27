package com.appkernel.json;

import org.json.JSONArray;

import java.util.Iterator;

/**
 * Very narrow-case for Filterable iterator for JSONArray
 * @author vadim
 *
 * @param <T>
 */
public class JSONArrayIterator<T> implements Iterable<T>, Iterator<T> {
	
	private final JSONArray mJsonArray;
	private final int mLength;
	private final Class<T>  mClazz;
	
	private int nextNonNull = -1;
	
	/**
	 * 
	 * @param jsonArray
	 * @param clazz
	 */
	public JSONArrayIterator(final JSONArray jsonArray, Class<T> clazz) {
		mJsonArray = jsonArray;
		mClazz     = clazz;
		mLength    = jsonArray == null ? 0 : jsonArray.length();
		moveToNextNonNull();
	}
	
	/**
	 * 
	 */
	private void moveToNextNonNull() {
		nextNonNull++;
		while (nextNonNull < mLength) {
			final Object o = mJsonArray.opt(nextNonNull);
			if (o != null && mClazz.isInstance(o))
				return;
			nextNonNull++;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return this;
	}

	@Override
	public boolean hasNext() {
		return nextNonNull < mLength;
	}

	@Override
	public T next() {
		@SuppressWarnings("unchecked")
		final T obj = (T) mJsonArray.opt(nextNonNull);
		moveToNextNonNull();
		return obj;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("JSONArrayIterator is readonly");
	}

}

package com.appkernel.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollectionUtils {
	
	/**
	 * Find all items in a list, matching filter
	 * 
	 * @param list
	 * @param filter
	 * @return
	 * 
	 * TODO: use Collection instead of List
	 */
	public static @NonNull <T> List<T> findAll(List<T> list, @Nullable Filterable<T> filter) {
		if (list == null)
			return Collections.emptyList();
		
		if (filter == null)
			return new ArrayList<T>(list); // Return copy of the list
		
		final List<T> res = new ArrayList<T>();
		for (T it : list)
			if (filter.match(it))
				res.add(it);

		return res;
	}
	
	/**
	 * Find first item in a list, matching filter
	 * @param list
	 * @param filter
	 * @return
	 * 
	 * TODO: use Collection instead of List 
	 */
	public static @Nullable <T> T findFirst(List<T> list, @Nullable Filterable<T> filter) {
		if (list == null || list.isEmpty())
			return null;
		
		if (filter == null)
			return list.get(0);
		
		for (T it: list) {
			if (filter.match(it))
				return it;
		}
		
		return null;
	}

}

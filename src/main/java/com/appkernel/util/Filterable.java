package com.appkernel.util;

public interface Filterable<T> {
	
	boolean match(T obj);

}

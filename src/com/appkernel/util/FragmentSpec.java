package com.appkernel.util;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;

public class FragmentSpec {
	final Bundle arguments = new Bundle();
	final String clazz;
	
	/**
	 * 
	 * @param clazz - Should be class that extents android.support.v4.app.Fragment or android.app.Fragment  
	 */
	public FragmentSpec(@NonNull Class<?> clazz) {
		this.clazz = clazz.getName();
	}
	
	public FragmentSpec(String clazz) {
		this.clazz = clazz;
	}
	
	public FragmentSpec put(String key, String value) {
		arguments.putString(key, value);
		return this;
	}
	
	public FragmentSpec put(String key, int value) {
		arguments.putInt(key, value);
		return this;
	}
	
	public FragmentSpec put(Bundle bundle) {
		arguments.putAll(bundle);
		return this;
	}
	
	public FragmentSpec put(String key, Serializable value) {
		arguments.putSerializable(key, value);
		return this;
	}
	
	public FragmentSpec put(String key, Parcelable value) {
		arguments.putParcelable(key, value);
		return this;
	}
	
	public String clazz() {
		return clazz;
	}
	
	public Bundle arguments() {
		return arguments;
	}
	
}
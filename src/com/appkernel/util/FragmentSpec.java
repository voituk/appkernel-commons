package com.appkernel.util;

import java.io.Serializable;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FragmentSpec {
	final Bundle arguments = new Bundle();
	final String clazz;
	
	public FragmentSpec(Class<? extends Fragment> clazz) {
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
	
	public String clazz() {
		return clazz;
	}
	
	public Bundle arguments() {
		return arguments;
	}
	
}
package com.appkernel.json;

import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appkernel.json.JSONArrayIterator;

public class JSONListAdapter extends ArrayAdapter<JSONObject> {
	
	
	private final LayoutInflater mInflater;
	private final String mField;
	private final int spinner_item_layout;
	private final int spinner_dropdown_item_layout;
	
	
	public JSONListAdapter(Context context) {
		super(context, android.R.layout.simple_spinner_item);
		mField    = null;
		mInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		spinner_item_layout          = android.R.layout.simple_spinner_item;
		spinner_dropdown_item_layout = android.R.layout.simple_spinner_dropdown_item;
	}
	
	
	public JSONListAdapter(Context context, final String field) {
		super(context, android.R.layout.simple_spinner_item);
		this.mField = field;
		mInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		spinner_item_layout          = android.R.layout.simple_spinner_item;
		spinner_dropdown_item_layout = android.R.layout.simple_spinner_dropdown_item;
		
	}
	
	
	public JSONListAdapter(Context context, final int spinner_layout, final int spinner_dropdown_layout) {
		super(context, spinner_layout);
		this.mField = null;
		mInflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		spinner_item_layout          = spinner_layout;
		spinner_dropdown_item_layout = spinner_dropdown_layout;		
	}
	
	
	public synchronized void setAll(Collection<? extends JSONObject> collection) {
		this.clear();
		this.addAllCompat11(collection);	
	}
	
	public synchronized void setAll(JSONArray collection) {
		this.clear();
		this.addAllCompat11(collection);
	}
	
	private synchronized void addAllCompat11(Collection<? extends JSONObject> collection) {
		setNotifyOnChange(false);
		for (JSONObject it : collection)
			add(it);
		setNotifyOnChange(true); // BULLSHIT! if true, modifications to the list will automatically call {@link notifyDataSetChanged}
		notifyDataSetChanged();
	}
	
	private synchronized void addAllCompat11(JSONArray collection) {
		if (collection == null)
			return;
		setNotifyOnChange(false);
		
		for(JSONObject it: new JSONArrayIterator<JSONObject>(collection, JSONObject.class)) {
			add(it);
		}
		
		setNotifyOnChange(true);
		notifyDataSetChanged();
	}
	
	protected CharSequence itemToString(final JSONObject it) {
		return TextUtils.isEmpty(mField) ? it.toString() : it.optString(mField);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, spinner_item_layout);
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, spinner_dropdown_item_layout);
	}
	
	private View createViewFromResource(int position, View convertView, ViewGroup parent, int resourceId) {
		JSONObject it = getItem(position);
		if (it == null) 
			return null;
		
		View v = convertView == null
				? mInflater.inflate(resourceId, parent, false)
				: convertView;
		
		((TextView) v.findViewById(android.R.id.text1)).setText( itemToString(it) );
		
		return v;
	}
	
	
	
}

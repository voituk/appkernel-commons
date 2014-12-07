package com.appkernel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class AwesomeButton extends Button {

	private CharSequence icon  = "";
	
	public AwesomeButton(Context context) {
		super(context);
		init(context);
	}
	
	public AwesomeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);

		icon = AwesomeText.getIconAttributeValue(context, attrs);
		
		setText(AwesomeText._text(icon, super.getText())); 
	}
	
	private void init(Context context) {
		if (isInEditMode())
			return;
		setTypeface(AwesomeText.Font.get(context));
	}
	
	
	/**
	 * Use this instead of Fucking final setText()!!!
	 * @param text
	 */
	public void setTextWithIcon(CharSequence text) {
		setText(AwesomeText._text(icon, text));
	}
}


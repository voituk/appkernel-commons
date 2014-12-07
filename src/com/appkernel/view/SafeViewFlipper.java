package com.appkernel.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

public class SafeViewFlipper extends ViewFlipper {

	public SafeViewFlipper(Context context) {
		super(context);
	}

	public SafeViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDetachedFromWindow() {
		try {
			super.onDetachedFromWindow();
		} catch (Throwable e) {
		}
	}

}

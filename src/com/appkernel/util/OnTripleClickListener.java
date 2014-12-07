package com.appkernel.util;

import android.view.View;
import android.view.View.OnClickListener;

public class OnTripleClickListener implements OnClickListener {

	private static final long CLICK_THRESHOLD = 500; 
	
	// All method of this class called from UI Thread - so, no need to synchronization 
	private int  click = 0;
	private long time  = 0;
	private OnClickListener mListener;
	
	public OnTripleClickListener(OnClickListener onClickListener) {
		mListener = onClickListener;
	}

	@Override
	public void onClick(View v) {
		final long now = System.currentTimeMillis();
		final long delta = now - time;
		
		if (delta > CLICK_THRESHOLD) {
			click = 1;
			
		} else {
			click++;
			if (click >= 3) {
				mListener.onClick(v);
				click = 0;
			}
		}
		
		time = now;
		
	}

}

package com.appkernel.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.appkernel.v7.Patterns;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Linkify2 {
	
	private static final class InternalURLSpan extends ClickableSpan {
	    final public String text;
	    
	    public InternalURLSpan(String text) {
	    	this.text  = text;
	    }

	    @Override
	    public void onClick(View widget) {
	    	final Context ctx = widget.getContext();
	    	try {	    	
		        ctx.startActivity(
		        	Intent.createChooser(
		        		new Intent(Intent.ACTION_VIEW, Uri.parse(text.trim())), 
		                null 
		        	)
		        );
	    	} catch (Exception e) {
	    		Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
	    }
	}


	private static void addLinks(TextView tv, Pattern pattern, String urlPrefix, MatchFilter matchFilter) {
		Spannable ss = new SpannableString(tv.getText());
		
		Matcher match = pattern.matcher(ss);
		while ( match.find() ) {
	        int x = match.start();
	        int y = match.end();
	        if (matchFilter==null || matchFilter.acceptMatch(ss, x, y) ) {	
	        	ss.setSpan(new InternalURLSpan( (urlPrefix==null ? "" : urlPrefix) + match.group(0)), x, y,  Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	        }
		}
		
		tv.setText(ss);
	}
	
	
	/**
	 * WARNING: This implementation still do not support overlying spans 
	 * For example: email@domain.com creates 2 spans: "email@domain.com" and "domain.com"
	 * 
	 * @param tv
	 */
	public static void addLinks(TextView tv) {
		addLinks(tv, Patterns.WEB_URL, null, Linkify.sUrlMatchFilter);
		addLinks(tv, Patterns.EMAIL_ADDRESS, "mailto:", null);

	    tv.setLinksClickable(true);
	    tv.setMovementMethod(LinkMovementMethod.getInstance());
	    tv.setFocusable(false);
	    
	}

}

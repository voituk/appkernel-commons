package com.appkernel.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.appkernel.commons.R;

public class Dialogs {

	private static final String	TAG	= Dialog.class.getName();

	/**
	 * Running in activity UI thread
	 * 
	 * @param activity
	 * @param title
	 * @param message
	 * @param onOkListener
	 *            May be <code>NULL</code>
	 */
	public static void alert(final Activity activity, final CharSequence title, final CharSequence message,
			final DialogInterface.OnClickListener onOkListener) {
		if (activity == null || activity.isFinishing())
			return;

		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
						.setPositiveButton(android.R.string.ok, onOkListener).show();
			}
		});

	}

	/**
	 * FIXME: Make it running in UIThread
	 * @param ctx
	 * @param title
	 * @param message
	 * @param listener
	 * @param yesno
	 */
	public static void confirm(@Nullable final Context ctx, CharSequence title, CharSequence message, DialogInterface.OnClickListener listener, @StringRes int... yesno) {
		if (ctx == null)
			return;
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(message)
					.setPositiveButton(yesno != null && yesno.length > 0 ? yesno[0] : android.R.string.yes, listener)
					.setNegativeButton(yesno != null && yesno.length > 1 ? yesno[1] : android.R.string.no, listener);

			if (!TextUtils.isEmpty(title))
				builder.setTitle(title);

			builder.create().show();
		} catch (Throwable e) {
			Log.e(TAG, "Dialogs.confirm(): " + e.getMessage());
		}
	}


	
	/**
	 * FIXME: Make it running in UIThread
	 * @param ctx
	 * @param title
	 * @param message
	 * @param listener
	 * @param yesno
	 */
	public static void confirm(@Nullable final Context ctx, CharSequence title, View view, DialogInterface.OnClickListener listener, @StringRes int... yesno) {
		if (ctx == null)
			return;
		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setView(view)
					.setPositiveButton(yesno != null && yesno.length > 0 ? yesno[0] : android.R.string.yes, listener)
					.setNegativeButton(yesno != null && yesno.length > 1 ? yesno[1] : android.R.string.no, listener);

			if (!TextUtils.isEmpty(title))
				builder.setTitle(title);

			builder.create().show();
		} catch (Throwable e) {
			Log.e(TAG, "Dialogs.confirm(): " + e.getMessage());
		}
	}



	/**
	 * 
	 * @param @Nullable ctx
	 * @param titleId
	 * @param messageId
	 * @param listener
	 * @param yesno
	 */
	public static void confirm(@Nullable final Context ctx, int titleId, int messageId, DialogInterface.OnClickListener listener, int... yesno) {
		confirm(ctx, titleId > 0 ? ctx.getString(titleId) : null, ctx.getString(messageId), listener, yesno);
	}

	
	
	/**
	 * FIXME: Make it running in UIThread
	 * 
	 * @param ctx
	 * @param cancelListener
	 * @return
	 */
	public static Dialog loading(@Nullable final Context ctx, OnCancelListener cancelListener) {
		final Dialog dialog  = cancelListener == null
			? ProgressDialog.show(ctx, null, ctx.getString(R.string.loading), true, false, null)
			: ProgressDialog.show(ctx, null, ctx.getString(R.string.loading), true, true, cancelListener);
			
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	
	
	/**
	 * Safe dialog close
	 * 
	 * @param @Nullable dialog
	 */
	public static void dismiss(@Nullable final Dialog dialog) {
		try {
			if (dialog != null)
				dialog.dismiss();
		} catch (Exception e) {
			Log.w(TAG, e.getMessage());
		}
	}

	/**
	 * 
	 * @param @Nullable activity
	 * @param dialogid
	 */
	@SuppressWarnings("deprecation")
	public static void dismiss(@Nullable Activity activity, int dialogid) {
		try {
			if (activity != null)
				activity.dismissDialog(dialogid);
		} catch (Exception e) {
			Log.w(TAG, e.getMessage());
		}
	}

}

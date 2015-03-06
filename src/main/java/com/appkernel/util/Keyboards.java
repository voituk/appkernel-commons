package com.appkernel.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 *
 */
public class Keyboards {

    /**
     * Show keyboard
     *
     * @param ctx
     */
    public static void show(final Context ctx) {
        if (ctx != null)
            ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Hide keyboard
     *
     * @param ctx
     */
    public static void hide(final Context ctx) {
        if (ctx != null)
            ((InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}
}

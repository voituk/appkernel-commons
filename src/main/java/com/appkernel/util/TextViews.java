package com.appkernel.util;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 */
public class TextViews {


    public static void strikeText(TextView text, boolean strike) {
        int flags = text.getPaintFlags();
        text.setPaintFlags(strike ? flags | Paint.STRIKE_THRU_TEXT_FLAG : flags & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    // TODO: Optimize this a bit if possible
    public static void text(View v, CharSequence str) {
        if (v == null)
            return;

        if (TextUtils.isEmpty(str)) {
            v.setVisibility(View.GONE);
            return;
        }

        TextView view = (TextView) v;
        view.setText(str);
        view.setVisibility(View.VISIBLE);
    }
}

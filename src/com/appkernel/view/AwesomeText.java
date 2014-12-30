package com.appkernel.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.appkernel.commons.R;

public class AwesomeText extends TextView {

    public static class Font {

        private static Typeface awesomeFont;

        public static synchronized Typeface get(Context context) {
            if (awesomeFont == null) {
                awesomeFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.AwesomeText_FontFile));
            }
            return awesomeFont;
        }
    }

    /**
     * @return Icon special character or empty string if no icon defined
     */
    static CharSequence getIconAttributeValue(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AwesomeText);

        StringBuilder icon = null;

        // iconId
        int code = a.getInteger(R.styleable.AwesomeText_iconId, 0);
        if (code > 0) {
            icon = new StringBuilder();
            for (char c : Character.toChars(code)) {
                icon.append(c);
            }
        }

        a.recycle();

        return icon == null ? "" : icon;
    }

    public static CharSequence _text(CharSequence icon, CharSequence text) {
        final StringBuilder s = new StringBuilder();

        if (icon != null && icon.length() > 0) {
            s.append(icon).append("  ");
        }

        return s.append(text.toString().replaceAll("і", "i")); // replace Ukrainian i to latin i because FontAwesome do not support ukrainian one
    }


    private CharSequence icon = "";

    public AwesomeText(Context context) {
        super(context);
        init(context);
    }

    public AwesomeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        icon = getIconAttributeValue(context, attrs);

        setText(_text(icon, super.getText()));
    }

    /**
     * Use this instead of Fucking final setText()!!!
     *
     * @param text
     */
    public void setTextWithIcon(CharSequence text) {
        setText(_text(icon, text));
    }

    private void init(Context context) {
        if (isInEditMode())
            return;
        setTypeface(Font.get(context));
    }

}


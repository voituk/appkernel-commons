package com.appkernel.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.appkernel.commons.R;

/**
 * Overrides {@link android.widget.ImageView#onMeasure(int, int)} and preserve certain aspect ratio of image
 *
 * @see com.looplr.components.ImageViewSquare
 * @see com.looplr.components.ImageView43Ratio
 * @author avasilenko
 * @date 25.4.14
 */
public class ImageViewByRatio extends ImageView {
    public static final int CALCULATE_BY_NONE = 0;
    public static final int CALCULATE_BY_WIDTH = 1;
    public static final int CALCULATE_BY_HEIGHT = 2;
    private int calculateBy;
    private int mForcedWidth;
    private int mForcedHeight;
    protected int widthRatio;
    protected int heightRatio;

    public ImageViewByRatio(Context context) {
        super(context);
    }

    public ImageViewByRatio(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributes(context, attrs);
    }

    public ImageViewByRatio(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        readAttributes(context, attrs);
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ImageViewByRatio,
                0, 0);
        calculateBy = a.getInteger(R.styleable.ImageViewByRatio_calculateBy, CALCULATE_BY_NONE);
        widthRatio = a.getInteger(R.styleable.ImageViewByRatio_widthRatio, 1);
        heightRatio = a.getInteger(R.styleable.ImageViewByRatio_heightRatio, 1);
        a.recycle();
    }

    public void setCalculateBy(int calculateBy) {
        this.calculateBy = calculateBy;
        requestLayout();
    }

    public void forceDimensions(int width, int height) {
        mForcedWidth = width;
        mForcedHeight = height;
    }

    public void resetForcedDimensions() {
        mForcedWidth = 0;
        mForcedHeight = 0;
        requestLayout();
    }

    public void setRatio(int width, int height) {
        widthRatio = width;
        heightRatio = height;
        requestLayout();
    }

    @Override
    protected final void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mForcedWidth != 0 && mForcedHeight != 0) {
            setMeasuredDimension(mForcedWidth, mForcedHeight);
        } else if (calculateBy == CALCULATE_BY_HEIGHT) {
            setMeasuredDimension(getMeasuredHeight() * widthRatio / heightRatio, getMeasuredHeight());
        } else if (calculateBy == CALCULATE_BY_WIDTH) {
            setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth() * heightRatio / widthRatio);
        }
    }
}
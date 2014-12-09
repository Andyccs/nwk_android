package com.nwk.locopromo.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.RoundedImageView;

public class SquareImageView extends RoundedImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int newWidth = getMeasuredWidth();

        setMeasuredDimension(newWidth, newWidth);
    }
}

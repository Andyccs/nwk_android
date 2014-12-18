package com.nwk.locopromo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ParallaxImageView extends ImageView {
    private int mCurrentTranslation;

    public ParallaxImageView(Context context) {
        super(context);
    }

    public ParallaxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    final double fMAX_COLORFILTER_ALPHA = 128;

    public void setCurrentTranslation(int currentTranslation) {
        mCurrentTranslation = currentTranslation;
        float ratio = mCurrentTranslation / (float) getHeight();
        int color = Color.argb((int) (fMAX_COLORFILTER_ALPHA * ratio), 0, 0, 0);
        if (color < 0)
            color = 0;
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(0, mCurrentTranslation / 2);
        super.draw(canvas);
        canvas.restore();
    }

}
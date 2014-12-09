package com.nwk.locopromo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.nwk.locopromo.R;

/**
 * Created by Andy on 12/9/2014.
 */
public class SelectableButton extends Button{
    boolean isSelected;

    public enum Side{
        LEFT_MOST,NONE,RIGHT_MOST
    };

    private Side side;

    public SelectableButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        side = Side.NONE;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SelectableButton,
                0, 0);

        try {
            int pos = a.getInteger(R.styleable.SelectableButton_position, 1);
            if(pos==0){
                side = Side.LEFT_MOST;
            }else if(pos==2){
                side = Side.RIGHT_MOST;
            }else{
                side = Side.NONE;
            }
        } finally {
            a.recycle();
        }
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if(isSelected){
            if(side==Side.LEFT_MOST){
                setBackgroundResource(R.drawable.category_background_leftmost_selected);
            }
            else if(side==Side.NONE){
                setBackgroundResource(R.drawable.category_background_selected);
            }
            else if(side==Side.RIGHT_MOST){
                setBackgroundResource(R.drawable.category_background_rightmost_selected);
            }
            setTextColor(getResources().getColor(android.R.color.white));
        }
        else{
            if(side==Side.LEFT_MOST){
                setBackgroundResource(R.drawable.category_background_leftmost);
            }
            else if(side==Side.NONE){
                setBackgroundResource(R.drawable.category_background);
            }
            else if(side==Side.RIGHT_MOST){
                setBackgroundResource(R.drawable.category_background_rightmost);
            }
            setTextColor(getResources().getColor(android.R.color.black));
        }
    }
}

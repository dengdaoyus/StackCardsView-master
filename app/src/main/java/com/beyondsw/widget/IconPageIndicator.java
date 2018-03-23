package com.beyondsw.widget;

/**
 * Created by Administrator on 2018/3/23 0023.
 */


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class IconPageIndicator extends LinearLayout  {


    public IconPageIndicator(Context context) {
        this(context, null);
    }

    public IconPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCurrentCount(int currentCount) {
        removeAllViews();
        for (int i = 0; i < currentCount; i++) {
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin=8;
            params.rightMargin=8;
            params.weight=1;
            ImageView view = new ImageView(getContext());
            view.setBackgroundResource(R.drawable.vpi__tab_indicator);
            view.setLayoutParams(params);
          addView(view);
        }
        requestLayout();
    }

    public void setCurrentItem(int item) {
        int tabCount = getChildCount();
        for (int i = 0; i < tabCount; i++) {
            View child = getChildAt(i);
            boolean isSelected = (i == item);
            child.setSelected(isSelected);
        }
    }
}

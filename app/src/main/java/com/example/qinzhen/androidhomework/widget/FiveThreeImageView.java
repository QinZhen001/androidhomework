package com.example.qinzhen.androidhomework.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by PandaQ on 2016/10/25.
 * email : 767807368@qq.com
 * 长宽比为4:3的imageView
 */

public class FiveThreeImageView extends android.support.v7.widget.AppCompatImageView {
    public FiveThreeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FiveThreeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) * 3 / 5, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, height);
    }
}

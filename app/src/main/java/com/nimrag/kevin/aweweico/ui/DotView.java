package com.nimrag.kevin.aweweico.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.nimrag.kevin.aweweico.R;

/**
 * Created by sunkevin on 2017/3/28.
 * 指示器中的小圆点
 */

public class DotView extends View {

    private boolean selected = false;
    Drawable normalDotDrawable;
    Drawable selectedDotDrawable;

    public DotView(Context context) {
        this(context, null, 0);
    }

    public DotView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        normalDotDrawable = ContextCompat.getDrawable(context, R.drawable.dot_indicator_normal);
        selectedDotDrawable = ContextCompat.getDrawable(context, R.drawable.dot_indicator_selected);
    }

    /**
     * 根据状态来绘制不同的图形
     * normal状态：空心圆
     * selected状态：实心圆
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (selected) {
            selectedDotDrawable.setBounds(0, 0, width, height);
            selectedDotDrawable.draw(canvas);
        } else {
            normalDotDrawable.setBounds(0, 0, width, height);
            normalDotDrawable.draw(canvas);
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        invalidate();
    }
}

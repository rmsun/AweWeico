package com.nimrag.kevin.aweweico.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.nimrag.kevin.aweweico.lib.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunkevin on 2017/3/28.
 * 圆点指示器
 */

public class DotViewIndicator extends LinearLayout {

    private int dotNum;
    private int prevSelectedIndex;
    private Context context;
    private List<View> dotViews = new ArrayList<View>();

    public DotViewIndicator(Context context) {
        this(context, null);
    }

    public DotViewIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setDotNumbers(int num) {
        this.dotNum = num;
        initDotView();
    }

    private void initDotView() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(Utils.dp2px(context, 10.0f), Utils.dp2px(context, 10.0f));

        for (int i = 0; i < dotNum; i++) {
            DotView dot = new DotView(context);
            if (i == 0) {
                layoutParams.leftMargin = 0;
            } else {
                layoutParams.leftMargin = Utils.dp2px(context, 5.0f);
            }

            dotViews.add(dot);
            addView(dot, layoutParams);
        }
    }

    public void setCurrentIndex(int index) {
        if (index < 0 || index >= dotNum) {
            return;
        }

        dotViews.get(prevSelectedIndex).setSelected(false);
        dotViews.get(index).setSelected(true);
        prevSelectedIndex = index;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dotViews.clear();
        dotViews = null;
    }
}

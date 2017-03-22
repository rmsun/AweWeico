package com.nimrag.kevin.aweweico.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.nimrag.kevin.aweweico.lib.Utils;

/**
 * Created by kevin on 2017/3/21.
 * 九宫格图片layout
 */

public class NineImageGridLayout extends ViewGroup {

    private static final float padding = 5.0f;

    public NineImageGridLayout(Context context) {
        this(context, null, 0);
    }

    public NineImageGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineImageGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 宽度为parent的宽度
     * 高度根据图片的数量确定
     * 1张时高度为图片自身的高度
     * 2张时高度为120dp
     * 3张时高度为100dp
     * 4/5/6张时高度为 100*2 dp
     * 7/8/9张时高度为 100*3 dp
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int height = heightMeasureSpec;
        int size = 0;

        if (childCount == 1) {
            // 测量子view的宽高
            measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec);
            height = getChildAt(0).getHeight();
        } else if (childCount == 2) {
            height = Utils.dp2px(getContext(), 120.0f);
            size = Utils.dp2px(getContext(), 120.0f);
        } else if (childCount == 3) {
            height = Utils.dp2px(getContext(), 100.0f);
            size = Utils.dp2px(getContext(), 100.0f);
        } else if (childCount == 4 || childCount == 5 || childCount == 6) {
            height = Utils.dp2px(getContext(), 200.0f + padding);
            size = Utils.dp2px(getContext(), 100.0f);
        } else {
            height = Utils.dp2px(getContext(), 300.f + padding * 2);
            size = Utils.dp2px(getContext(), 100.0f);
        }

        // 设置子view的宽高相同
        if (size != 0) {
            for (int i = 0; i < childCount; i++) {
                measureChild(getChildAt(i), size, size);
            }
        }

        // 保存宽高
        setMeasuredDimension(widthMeasureSpec, height);
    }

    /**
     * 以九宫格的方式layout children
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        switch (childCount) {
            case 1:
                left = l;
                top = t;
                right = left + getChildAt(0).getWidth();
                bottom = top + getChildAt(0).getHeight();
                getChildAt(0).layout(left, top, right, bottom);
                break;
            case 2:
            case 4:
                for (int i = 0; i < childCount; i++){
                    View child = getChildAt(i);
                    left = l + (child.getWidth() + (int) padding) * (i % 2);
                    top = t + (child.getHeight() + (int) padding) * (i / 2);
                    right = left + child.getWidth();
                    bottom = top + child.getHeight();
                    child.layout(left, top, right, bottom);
                }
                break;
            // 3,5,6,7,8,9
            default:
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    left = l + (child.getWidth() + (int) padding) * (i % 3);
                    top = t + (child.getHeight() + (int) padding) * (i / 3);
                    right = left + child.getWidth();
                    bottom = top + child.getHeight();
                    child.layout(left, top, right, bottom);
                }
        }
    }
}

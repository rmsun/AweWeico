package com.nimrag.kevin.aweweico.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nimrag.kevin.aweweico.lib.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/3/21.
 * 九宫格图片layout
 */

public class NineImageGridLayout extends ViewGroup {

    private static final float padding = 5.0f;
    private ArrayList<String> largImageUrls = new ArrayList<String>();

    public NineImageGridLayout(Context context) {
        this(context, null, 0);
    }

    public NineImageGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NineImageGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLargImageUrls(ArrayList<String> urls) {
        largImageUrls = urls;
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
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        int height = heightMeasureSpec;
        int size = 0;

        if (childCount == 1) {
            // 测量子view的宽高
            measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec);
            height = getChildAt(0).getMeasuredHeight() * 4;
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
                measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
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
        /**
         * 子view的宽高
         */
        int width = 0;
        int height = 0;

        switch (childCount) {
            case 1:
                right = left + getChildAt(0).getMeasuredWidth() * 4;
                bottom = top + getChildAt(0).getMeasuredHeight() * 4;
                getChildAt(0).layout(left, top, right, bottom);
                break;
            case 2:
                width = Utils.dp2px(getContext(), 120.0f);
                height = width;
                for (int i = 0; i < childCount; i++){
                    View child = getChildAt(i);
                    left = (width + (int) padding) * (i % 2);
                    top = 0;
                    right = left + width;
                    bottom = top + height;
                    child.layout(left, top, right, bottom);
                }
                break;
            case 4:
                width = Utils.dp2px(getContext(), 100.0f);
                height = width;
                for (int i = 0; i < childCount; i++){
                    View child = getChildAt(i);
                    left = (width + (int) padding) * (i % 2);
                    top = (height + (int) padding) * (i / 2);
                    right = left + width;
                    bottom = top + height;
                    child.layout(left, top, right, bottom);
                }
                break;
            // 3,5,6,7,8,9
            default:
                width = Utils.dp2px(getContext(), 100.0f);
                height = width;
                for (int i = 0; i < childCount && i < 9; i++) {
                    View child = getChildAt(i);
                    left = (width + (int) padding) * (i % 3);
                    top = (height + (int) padding) * (i / 3);
                    right = left + width;
                    bottom = top + height;
                    child.layout(left, top, right, bottom);
                }
        }
    }

    /**
     * 拦截touch event
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                // 开启activity，展示大图。通过坐标判断点击子view的index
                int touchIndex = 0;
                int x = Math.round(event.getX());
                int y = Math.round(event.getY());
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (x > child.getLeft() && x < child.getRight() && y > child.getTop() && y < child.getBottom()) {
                        //touch is within this child
                        touchIndex = i;
                    }
                }
                Log.d("haha", "largeImageUrls: " + largImageUrls + "touchIndex: " + touchIndex);
                Intent intent = new Intent(getContext().getApplicationContext(), PhotoViewActivity.class);
                intent.putExtra("imageUrls", largImageUrls);
                intent.putExtra("initIndex", touchIndex);
                getContext().getApplicationContext().startActivity(intent);
                break;
            default:
                break;
        }
        return true;
    }
}

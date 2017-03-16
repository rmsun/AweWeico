package com.nimrag.kevin.aweweico.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kevin on 2017/2/18.
 */


public abstract class QuickAdapter<T> extends RecyclerView.Adapter<QuickAdapter.VH> {

    private List<T> mDatas;
    private View mHeaderView = null;
    private View mFooterView = null;
    private boolean mHasHeader = false;
    private boolean mHasFooter = false;

    enum ITEM_TYPE {
        HEADER,
        FOOTER,
        NORMAL
    };

    public QuickAdapter(List<T> datas){
        this.mDatas = datas;
    }

    public abstract int getLayoutId(int viewType);
    public void setHeaderView(View headerView) { this.mHeaderView = headerView; }
    public void setFooterView(View footerView) { this.mFooterView = footerView; }
    public void setHasHeader(boolean value) { mHasHeader = value; }
    public void setHasFooter(boolean value) { mHasFooter = value; }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH viewHolder = VH.get(parent, getLayoutId(viewType));
        if (viewType == ITEM_TYPE.HEADER.ordinal()) {
            mHeaderView = viewHolder.getContentView();
        } else if (viewType == ITEM_TYPE.FOOTER.ordinal()) {
            mFooterView = viewHolder.getContentView();
        }
        return viewHolder;
    }

    // 需要处理以下几种情况：
    // 1. 既无header又无footer
    // 2. 只有header
    // 3. 只有footer
    // 4. 既有header又有footer
    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHasHeader) {
            return ITEM_TYPE.HEADER.ordinal();
        } else if (position == getItemCount() - 1 && mHasFooter) {
            return ITEM_TYPE.FOOTER.ordinal();
        } else {
            return ITEM_TYPE.NORMAL.ordinal();
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Log.d("haha", "onBindViewHolder position " + position);
        if(position == 0 && mHasHeader) {
            return;
        } else if (position == getItemCount() - 1 && mHasFooter) {
            return;
        } else {
            if (mHasHeader) {
                convert(holder, mDatas.get(position - 1), position);
            } else {
                convert(holder, mDatas.get(position), position);
            }
        }
    }

    //@Override
    //public long getItemId(int position) {
        //return position;
    //}

    @Override
    public int getItemCount() {
        if (mHasHeader && mHasFooter) {
            return mDatas.size() + 2;
        } else if ( !mHasHeader && !mHasFooter) {
            return mDatas.size();
        } else {
            return mDatas.size() + 1;
        }

    }

    public abstract void convert(VH holder, T data, int position);

    static class VH extends RecyclerView.ViewHolder{
        private SparseArray<View> mViews;
        private View mConvertView;

        public VH(View v){
            super(v);
            mConvertView = v;
            mViews = new SparseArray<>();
        }

        public static VH get(ViewGroup parent, int layoutId){
            View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new VH(convertView);
        }

        public <T extends View> T getView(int id){
            View v = mViews.get(id);
            if(v == null){
                v = mConvertView.findViewById(id);
                mViews.put(id, v);
            }
            return (T)v;
        }

        public View getContentView() {
            return mConvertView;
        }

        public void setText(int id, String value){
            TextView view = getView(id);
            view.setText(value);
        }
    }
}

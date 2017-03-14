package com.nimrag.kevin.aweweico.ui;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nimrag.kevin.aweweico.R;
import com.nimrag.kevin.aweweico.lib.CircleImageTransformation;
import com.nimrag.kevin.aweweico.lib.Logger;
import com.nimrag.kevin.aweweico.lib.Utils;
import com.nimrag.kevin.aweweico.sinasdk.bean.FriendsTimeLine;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/2/17.
 */

public class TimeLineHomeFragment extends Fragment implements ITimeLineView {

    private RecyclerView mRecycleView;
    private QuickAdapter<FriendsTimeLine.StatusesBean> mAdapter;
    private List<FriendsTimeLine.StatusesBean> statusContent;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isRefresh = false;
    private ITimeLinePresenter mTimeLinePresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        mTimeLinePresenter = new TimeLinePresenter(this);
        mRecycleView = (RecyclerView) rootView.findViewById(android.R.id.list);
        mRecycleView.addOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMoreData() {
            }
        });

        statusContent = new ArrayList<FriendsTimeLine.StatusesBean>();
        mAdapter = new QuickAdapter<FriendsTimeLine.StatusesBean>(statusContent) {
            @Override
            public int getLayoutId(int viewType) {
                if (viewType == ITEM_TYPE.NORMAL.ordinal()) {
                    return R.layout.fragment_weibo_content;
                } else if (viewType == ITEM_TYPE.FOOTER.ordinal()) {
                    return R.layout.footer_view;
                }
                return -1;
            }

            @Override
            public void convert(VH holder, FriendsTimeLine.StatusesBean status, int position) {
                ImageView imageView = holder.getView(R.id.profile_image);
                TextView screenName = holder.getView(R.id.screen_name);
                TextView editText = holder.getView(R.id.weibo_content);
                GridLayout imageGridLayout = holder.getView(R.id.image_grid_layout);

                // 加载图片
                Picasso.with(getActivity().getApplicationContext()).load(status.getUser().getProfile_image_url()).transform(new CircleImageTransformation()).resize(150, 150).into(imageView);
                screenName.setText(status.getUser().getScreen_name());
                editText.setText(status.getText());
                /**
                 * 不同数量的图片对应不同的列数
                 * 1张图片,一行一列,保持图片的宽高比例
                 * 2/4张图片,两行两列
                 * 3/5/6/7/8/9 三列
                 * 优化:自定义控件实现
                 */
                loadTimeLineImage(imageGridLayout, status.getPic_urls());
            }
        };

        mAdapter.setHasStableIds(true);
        mAdapter.setHasFooter(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(mAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.fragment_home_pager);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                if(!isRefresh) {
                    isRefresh = true;
                    mTimeLinePresenter.refreshData();
                }
            }
        });
        mSwipeRefreshLayout.setRefreshing(true);
        mTimeLinePresenter.refreshData();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefreshData(FriendsTimeLine data) {
        mSwipeRefreshLayout.setRefreshing(false);
        isRefresh = false;
        List<FriendsTimeLine.StatusesBean> tempList = new ArrayList<FriendsTimeLine.StatusesBean>();
        if (data != null) {
            // 交换Array，可以优化
            List<FriendsTimeLine.StatusesBean> status = data.getStatuses();
            for (int i = 0; i < status.size(); i++) {
                tempList.add(status.get(i));
            }
            for (int i = 0; i < statusContent.size(); i++) {
                tempList.add(statusContent.get(i));
            }
            statusContent.clear();
            statusContent.addAll(tempList);
            mAdapter.notifyDataSetChanged();
        }
        tempList = null;
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreData(FriendsTimeLine data) {

    }

    /**
     * 以九宫格方式加载图片
     * @param gridLayout
     * @param picUrls
     */
    private void loadTimeLineImage(GridLayout gridLayout, List<FriendsTimeLine.picUrlsBean> picUrls) {

        gridLayout.removeAllViews();
        // 图片张数
        int imageSize = picUrls.size();

        if (imageSize == 0) {
            return;
        }

        // 图片为1张时，保持原图的宽高比
        if (imageSize == 1) {
            NormalBitmapTarget target = new NormalBitmapTarget(gridLayout);
            Picasso.with(getActivity().getApplicationContext()).load(picUrls.get(0).getThumbnail_pic()).placeholder(R.drawable.image_for_test_dong).into(target);
        } else if (imageSize == 2 || imageSize ==4) {
            gridLayout.setColumnCount(2);
            for (int i = 0; i < imageSize; i++) {
                SquareBitmapTarget target = new SquareBitmapTarget(gridLayout, i);
                Picasso.with(getActivity().getApplicationContext()).load(picUrls.get(i).getThumbnail_pic()).placeholder(R.drawable.image_for_test_dong).into(target);
            }
        } else {
            gridLayout.setColumnCount(3);
            for (int i = 0; i < imageSize; i++) {
                SquareBitmapTarget target = new SquareBitmapTarget(gridLayout, i);
                Picasso.with(getActivity().getApplicationContext()).load(picUrls.get(i).getThumbnail_pic()).placeholder(R.drawable.image_for_test_dong).into(target);
            }
        }
    }

    /**
     * 加载图片，保持宽高比，根据返回图片的大小设置gridlayout的大小
     */
    private class NormalBitmapTarget implements Target {

        private GridLayout gridLayout;

        public NormalBitmapTarget(GridLayout gridLayout) {
            super();
            this.gridLayout = gridLayout;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int cellWidth = (int)Math.round(width * 4);
            int cellHeight = (int)Math.round(height * 4);
            GridLayout.Spec rowSpec = GridLayout.spec(0);
            GridLayout.Spec columnSpec = GridLayout.spec(0);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(cellWidth, cellHeight));
            lp.rowSpec = rowSpec;
            lp.columnSpec = columnSpec;
            ImageView image = new ImageView(getActivity().getApplicationContext());
            image.setImageBitmap(bitmap);
            gridLayout.addView(image, lp);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            GridLayout.Spec rowSpec = GridLayout.spec(0);
            GridLayout.Spec columnSpec = GridLayout.spec(0);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lp.rowSpec = rowSpec;
            lp.columnSpec = columnSpec;
            ImageView image = new ImageView(getActivity().getApplicationContext());
            image.setImageDrawable(placeHolderDrawable);
            gridLayout.addView(image, lp);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }
    }

    /**
     * 加载方形图片
     */
    private class SquareBitmapTarget implements Target {

        private GridLayout gridLayout;
        private int imageIndex;

        public SquareBitmapTarget(GridLayout gridLayout, int index) {
            super();
            this.gridLayout = gridLayout;
            imageIndex = index;
        }
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            int columnCount = gridLayout.getColumnCount();
            GridLayout.Spec rowSpec = GridLayout.spec(imageIndex / columnCount);
            GridLayout.Spec columnSpec = GridLayout.spec(imageIndex % columnCount);
            int cellSize = Utils.dp2px(getActivity().getApplicationContext(), 100.0f);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(cellSize, cellSize));
            lp.setMargins(10, 10, 10, 10);
            lp.rowSpec = rowSpec;
            lp.columnSpec = columnSpec;
            SquareImageView image = new SquareImageView(getActivity().getApplicationContext());
            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setImageBitmap(bitmap);
            gridLayout.addView(image, lp);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }
    }
}

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
    private ArrayList<FriendsTimeLine.StatusesBean> mData;
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

        mData = new ArrayList<FriendsTimeLine.StatusesBean>();
        mAdapter = new QuickAdapter<FriendsTimeLine.StatusesBean>(mData) {
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
            public void convert(VH holder, FriendsTimeLine.StatusesBean data, int position) {
                ImageView imageView = holder.getView(R.id.profile_image);
                TextView screenName = holder.getView(R.id.screen_name);
                TextView editText = holder.getView(R.id.weibo_content);
                GridLayout imageGridLayout = holder.getView(R.id.image_grid_layout);

                // 加载图片
                Picasso.with(getActivity().getApplicationContext()).load(data.getUser().getProfile_image_url()).transform(new CircleImageTransformation()).resize(150, 150).into(imageView);
                screenName.setText(data.getUser().getScreen_name());
                editText.setText(data.getText());
                /**
                 * 不同数量的图片对应不同的列数
                 * 1张图片,一行一列,保持图片的宽高比例
                 * 2/4张图片,两行两列
                 * 3/5/6/7/8/9 三列
                 * 优化:自定义控件实现
                 */
                /*imageGridLayout.setColumnCount(2);
                for (int i = 0; i < 2; i++) {
                    GridLayout.Spec rowSpec = GridLayout.spec(i / imageGridLayout.getColumnCount());
                    GridLayout.Spec columnSpec = GridLayout.spec(i % imageGridLayout.getColumnCount(), 1.0f);
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(400, 200));
                    layoutParams.rowSpec = rowSpec;
                    layoutParams.columnSpec = columnSpec;
                    layoutParams.setMargins(50, 50, 50, 50);
                    ImageView testImage = new ImageView(getActivity().getApplicationContext());
                    testImage.setImageResource(R.drawable.image_for_test_dong);
                    Bitmap bp = ((BitmapDrawable)testImage.getDrawable()).getBitmap();
                    Log.d("haha", "bp width " + bp.getWidth() + " bp height " + bp.getHeight());

                    imageGridLayout.addView(testImage, layoutParams);
                }*/
                loadTimeLineImage(imageGridLayout, data.getPic_urls());
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
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipeRefreshLayout.setRefreshing(true);
        mTimeLinePresenter.refreshData();
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefreshData(FriendsTimeLine data) {
        mSwipeRefreshLayout.setRefreshing(false);
        isRefresh = false;
        if (data != null) {
            List<FriendsTimeLine.StatusesBean> status = data.getStatuses();
            for (int i = 0; i < status.size(); i++) {
                mData.add(status.get(i));
            }
            mAdapter.notifyDataSetChanged();
        }
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

        // 图片张数
        int imageSize = picUrls.size();

        // 图片为1张时，保持原图的宽高比
        if (imageSize == 1) {
            NormalBitmapTarget target = new NormalBitmapTarget(gridLayout);
            Picasso.with(getActivity().getApplicationContext()).load(picUrls.get(0).getThumbnail_pic()).into(target);
        } else if (imageSize == 2 || imageSize ==4) {
            if (imageSize == 2) {
                gridLayout.setColumnCount(2);
            } else {
                gridLayout.setColumnCount(4);
            }
            //GridLayout.LayoutParams lp = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //lp.setMargins(10, 10, 10, 10);
            for (int i = 0; i < imageSize; i++) {
                SquareBitmapTarget target = new SquareBitmapTarget(gridLayout, i);
                Picasso.with(getActivity().getApplicationContext()).load(picUrls.get(0).getThumbnail_pic()).into(target);
            }
        } else {
            gridLayout.setColumnCount(3);
            //GridLayout.LayoutParams lp = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //lp.setMargins(10, 10, 10, 10);
            for (int i = 0; i < imageSize; i++) {
                SquareBitmapTarget target = new SquareBitmapTarget(gridLayout, i);
                Picasso.with(getActivity().getApplicationContext()).load(picUrls.get(0).getThumbnail_pic()).into(target);
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
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            lp.setMargins(10, 10, 10, 10);
            lp.rowSpec = rowSpec;
            lp.columnSpec = columnSpec;
            SquareImageView image = new SquareImageView(getActivity().getApplicationContext());
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

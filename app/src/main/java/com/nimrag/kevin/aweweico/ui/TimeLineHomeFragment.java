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
            public void loadMoreData() {
                mTimeLinePresenter.loadMoreData();
            }
        });
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                final Picasso picasso = Picasso.with(getContext());
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    picasso.resumeTag(getContext());
                } else {
                    picasso.pauseTag(getContext());
                }
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
                TextView publishDate = holder.getView(R.id.publish_date);
                TextView editText = holder.getView(R.id.weibo_content);
                NineImageGridLayout imageGridLayout = holder.getView(R.id.image_grid_layout);
                TextView source = holder.getView(R.id.weibo_source);
                ImageView attitudeImage = holder.getView(R.id.attitude);
                TextView attitudeCount = holder.getView(R.id.attitude_count);
                ImageView repostImage = holder.getView(R.id.reposts);
                TextView repostCount = holder.getView(R.id.reposts_count);
                ImageView commentImage = holder.getView(R.id.comment);
                TextView commentCount = holder.getView(R.id.comment_count);

                // 加载头像
                Picasso picasso = Picasso.with(getContext());
                picasso.setIndicatorsEnabled(true);
                picasso.load(status.getUser().getProfile_image_url()).transform(new CircleImageTransformation()).resize(150, 150).into(imageView);
                screenName.setText(status.getUser().getScreen_name());
                publishDate.setText(Utils.convertTime(status.getCreated_at()));
                editText.setText(status.getText());
                source.setText(Utils.parseHtmlATag(status.getSource()));
                if (status.getAttitudes_count() == 0) {
                    attitudeImage.setVisibility(View.GONE);
                    attitudeCount.setVisibility(View.GONE);
                } else {
                    attitudeCount.setText(String.valueOf(status.getAttitudes_count()));
                }
                if (status.getReposts_count() == 0) {
                    repostImage.setVisibility(View.GONE);
                    repostCount.setVisibility(View.GONE);
                } else {
                    repostCount.setText(String.valueOf(status.getReposts_count()));
                }
                if (status.getComments_count() == 0) {
                    commentImage.setVisibility(View.GONE);
                    commentCount.setVisibility(View.GONE);
                } else {
                    commentCount.setText(String.valueOf(status.getComments_count()));
                }

                /**
                 * 不同数量的图片对应不同的列数
                 * 1张图片,一行一列,保持图片的宽高比例
                 * 2/4张图片,两行两列
                 * 3/5/6/7/8/9 三列
                 * 优化:自定义控件实现，移动网络下使用缩略图，wifi下使用中图
                 */
                imageGridLayout.removeAllViews();
                loadTimeLineImage(imageGridLayout, status.getPic_urls());
                ArrayList<String> largeImageUrls = new ArrayList<String>();
                for (int i = 0; i < status.getPic_urls().size(); i++) {
                    String thumbnailUrl = status.getPic_urls().get(i).getThumbnail_pic();
                    String largeImageUrl = thumbnailUrl.replace("thumbnail", "large");
                    largeImageUrls.add(largeImageUrl);
                }
                imageGridLayout.setLargImageUrls(largeImageUrls);
            }
        };

        //mAdapter.setHasStableIds(true);
        mAdapter.setHasFooter(true);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.setAdapter(mAdapter);
        mRecycleView.addItemDecoration(new DividerDecoration(getActivity(), DividerDecoration.VERTICAL_LIST));
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
        //mSwipeRefreshLayout.setRefreshing(true);
        //mTimeLinePresenter.refreshData();
        // 加载数据
        mTimeLinePresenter.refreshData();
        return rootView;
    }

    @Override
    public void startRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mTimeLinePresenter.refreshData();
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
            // 如果小于20条数据，直接push back，如果大于等于20条就直接替换
            if (status.size() < 20) {
                for (int i = 0; i < statusContent.size(); i++) {
                    tempList.add(statusContent.get(i));
                }
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
        statusContent.addAll(data.getStatuses());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 以九宫格方式加载图片
     * @param gridLayout
     * @param picUrls
     */
    private void loadTimeLineImage(NineImageGridLayout gridLayout, List<FriendsTimeLine.picUrlsBean> picUrls) {

        gridLayout.removeAllViews();
        // 图片张数
        int imageSize = picUrls.size();

        if (imageSize == 0) {
            return;
        }

        for (int i = 0; i < imageSize; i++) {
            NormalBitmapTarget target = new NormalBitmapTarget(gridLayout, i);
            ImageView image = new ImageView(getContext());
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String thumbnailImageUrl = picUrls.get(i).getThumbnail_pic();
            String largeImageUrl = thumbnailImageUrl.replace("thumbnail", "large");
            image.setTag(largeImageUrl);
            gridLayout.addView(image, i);
            Picasso.with(getContext()).load(thumbnailImageUrl).placeholder(R.drawable.place_holder).into(image);
        }
    }

    /**
     * 加载图片，保持宽高比，根据返回图片的大小设置gridlayout的大小
     */
    private class NormalBitmapTarget implements Target {

        private NineImageGridLayout gridLayout;
        private int imageIndex;

        public NormalBitmapTarget(NineImageGridLayout gridLayout, int index) {
            super();
            this.gridLayout = gridLayout;
            imageIndex = index;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            ImageView image = new ImageView(getActivity().getApplicationContext());
            image.setImageBitmap(bitmap);
            gridLayout.removeViewAt(imageIndex);
            gridLayout.addView(image, imageIndex);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            ImageView image = new ImageView(getActivity().getApplicationContext());
            image.setImageDrawable(placeHolderDrawable);
            gridLayout.addView(image, imageIndex);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }
    }
}

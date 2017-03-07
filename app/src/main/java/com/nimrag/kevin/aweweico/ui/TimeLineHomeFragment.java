package com.nimrag.kevin.aweweico.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nimrag.kevin.aweweico.R;
import com.nimrag.kevin.aweweico.sinasdk.bean.FriendsTimeLine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/2/17.
 */

public class TimeLineHomeFragment extends Fragment implements ITimeLineView {

    private RecyclerView mRecycleView;
    private QuickAdapter<WeiboItem> mAdapter;
    private ArrayList<WeiboItem> mData;
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

        mData = new ArrayList<WeiboItem>();
        mAdapter = new QuickAdapter<WeiboItem>(mData) {
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
            public void convert(VH holder, WeiboItem data, int position) {
                ImageView imageView = holder.getView(R.id.profile_image);
                TextView screenName = holder.getView(R.id.screen_name);
                TextView editText = holder.getView(R.id.weibo_content);

                //加载图片
                Picasso.with(getActivity().getApplicationContext()).load(data.getProfileImageUrl()).resize(150, 150).into(imageView);
                screenName.setText(data.getScreenName());
                editText.setText(data.getText());
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

    @Override
    public void onRefreshData(FriendsTimeLine data) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (data != null) {
            List<FriendsTimeLine.StatusesBean> status = data.getStatuses();
            for (int i = 0; i < status.size(); i++) {
                WeiboItem item = new WeiboItem(status.get(i).getUser().getProfile_image_url(), status.get(i).getUser().getScreen_name(), status.get(i).getText());
                mData.add(item);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMoreData(FriendsTimeLine data) {

    }
}

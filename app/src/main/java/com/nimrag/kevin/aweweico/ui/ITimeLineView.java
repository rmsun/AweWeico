package com.nimrag.kevin.aweweico.ui;

import com.nimrag.kevin.aweweico.sinasdk.bean.FriendsTimeLine;

/**
 * Created by kevin on 2017/3/6.
 */

public interface ITimeLineView {
    public void onRefreshData(FriendsTimeLine data);
    public void onLoadMoreData(FriendsTimeLine data);
    public void startRefresh();
}

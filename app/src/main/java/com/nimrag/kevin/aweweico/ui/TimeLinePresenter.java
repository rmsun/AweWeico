package com.nimrag.kevin.aweweico.ui;

import android.os.AsyncTask;

import com.nimrag.kevin.aweweico.App;
import com.nimrag.kevin.aweweico.lib.Params;
import com.nimrag.kevin.aweweico.sinasdk.SinaSDK;
import com.nimrag.kevin.aweweico.sinasdk.bean.FriendsTimeLine;

/**
 * Created by kevin on 2017/3/6.
 */

public class TimeLinePresenter implements ITimeLinePresenter {

    private ITimeLineView mTimeLineView;

    public TimeLinePresenter(ITimeLineView v) { mTimeLineView = v; }

    @Override
    public void refreshData() {
        LoadDataTask task = new LoadDataTask();
        task.execute(true);
    }

    @Override
    public void loadMoreData() {
        LoadDataTask task = new LoadDataTask();
        task.execute(false);
    }

    /**
     * 获取timeline数据AsyncTask
     * 参数 true 刷新数据 false加载更多
     */
    private class LoadDataTask extends AsyncTask<Boolean, Void, FriendsTimeLine> {
        private boolean refreshData;
        @Override
        protected FriendsTimeLine doInBackground(Boolean... params) {
            refreshData = params[0];
            Params urlParams = new Params();
            urlParams.addParam("access_token", App.getUserInfo().getAccessToken());
            if (refreshData) {
                urlParams.addParam("since_id", String.valueOf(App.getSinceId()));
            } else {
                urlParams.addParam("max_id", String.valueOf(App.getMaxId()));
            }
            return SinaSDK.getInstance().getFriendsTimeLine(urlParams);
        }

        @Override
        protected void onPostExecute(FriendsTimeLine friendsTimeLine) {
            super.onPostExecute(friendsTimeLine);
            // 刷新时更新since_id，加载更多时更新max_id，清空现有数据时需要同时更新since_id和max_id
            if (friendsTimeLine != null) {
                if (refreshData) {
                    App.setSinceId(friendsTimeLine.getSince_id());
                    mTimeLineView.onRefreshData(friendsTimeLine);
                } else {
                    App.setMaxId(friendsTimeLine.getMax_id());
                    mTimeLineView.onLoadMoreData(friendsTimeLine);
                }
            }
        }
    }
}

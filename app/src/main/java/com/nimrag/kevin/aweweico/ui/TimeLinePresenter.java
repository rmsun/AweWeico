package com.nimrag.kevin.aweweico.ui;

import android.os.AsyncTask;

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
        task.execute();
    }

    @Override
    public void loadMoreData() {

    }

    private class LoadDataTask extends AsyncTask<Void, Void, FriendsTimeLine> {
        @Override
        protected FriendsTimeLine doInBackground(Void... params) {
            return SinaSDK.getInstance().getFriendsTimeLine();
        }

        @Override
        protected void onPostExecute(FriendsTimeLine friendsTimeLine) {
            super.onPostExecute(friendsTimeLine);
            mTimeLineView.onRefreshData(friendsTimeLine);
        }
    }
}

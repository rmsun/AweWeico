package com.nimrag.kevin.aweweico.cache;

import android.content.SharedPreferences;

import com.nimrag.kevin.aweweico.App;
import com.nimrag.kevin.aweweico.lib.ActivitySharePrefHelper;
import com.nimrag.kevin.aweweico.lib.GlobalContext;
import com.nimrag.kevin.aweweico.lib.ICache;
import com.nimrag.kevin.aweweico.lib.IResult;
import com.nimrag.kevin.aweweico.lib.Params;
import com.nimrag.kevin.aweweico.lib.Utils;
import com.nimrag.kevin.aweweico.lib.orm.SqliteUtility;
import com.nimrag.kevin.aweweico.lib.orm.extra.Extra;
import com.nimrag.kevin.aweweico.lib.orm.utils.CacheTimeUtils;
import com.nimrag.kevin.aweweico.sinasdk.bean.FriendsTimeLine;

import java.util.List;

/**
 * Created by kevin on 2017/3/16.
 */

public class FriendsTimeLineCache implements ICache{

    private static FriendsTimeLineCache intance;

    /**
     * 懒汉模式,双重检测,保证线程安全
     */
    public static  FriendsTimeLineCache getInstance() {
        if (intance == null) {
            synchronized(FriendsTimeLineCache.class) {
                if (intance == null) {
                    intance = new FriendsTimeLineCache();
                }
            }
        }
        return intance;
    }

    private static final String key = "statuses/public_timeline";
    @Override
    public IResult getData(Params urlParams) {
        if (urlParams.getKeys().contains("since_id") || urlParams.getKeys().contains("max_id")) {
            return null;
        }
        Extra extra = new Extra(App.getUserInfo().getUid(), Utils.generateMD5(key));
        FriendsTimeLine statusContents = null;
        List<FriendsTimeLine.StatusesBean> statuses = SqliteUtility.getInstance("weiboDB").select(extra, FriendsTimeLine.StatusesBean.class);
        if (statuses.size() > 0) {
            statusContents = new FriendsTimeLine();
            statusContents.setStatuses(statuses);
            statusContents.setFromCache(true);
            statusContents.setOutOfDate(CacheTimeUtils.outOfDate(key));
            statusContents.setSince_id(Long.valueOf(ActivitySharePrefHelper.getShareData(GlobalContext.getGlobalContext(), "since_id")));
            statusContents.setMax_id(Long.valueOf(ActivitySharePrefHelper.getShareData(GlobalContext.getGlobalContext(), "max_id")));
        }
        return statusContents;
    }

    @Override
    public void setData(IResult result, Params urlParams) {
        /**
         * 缓存策略：刷新时返回微博信息的数量大于等于20条，先清空缓存，再写入。
         * 加载更多时，直接插库，数据库缓存的最大数目为60条
         * if (refresh) {
         *     if (dataSize >= 20)
         *         sqlite.deleteAll;
         * }
         * sqlite.add
         * 通过url参数中是否包含since_id和max_id来判断是刷新还是加载
         */
        FriendsTimeLine timeLineData = (FriendsTimeLine)result;
        Extra extra = new Extra(App.getUserInfo().getUid(), Utils.generateMD5(key));
        // 刷新
        if (urlParams.getKeys().contains("since_id")) {
            if (timeLineData.getStatuses().size() >= 20) {
                SqliteUtility.getInstance("weiboDB").deleteAll(extra, FriendsTimeLine.StatusesBean.class);
            }
        }
        SqliteUtility.getInstance("weiboDB").insert(extra, ((FriendsTimeLine)result).getStatuses());
        CacheTimeUtils.saveTime(key);
        // save since_id和max_id
        long sinceId = timeLineData.getSince_id();
        if (sinceId != 0) {
            ActivitySharePrefHelper.putShareData(GlobalContext.getGlobalContext(), "since_id", String.valueOf(sinceId));
        }
        if (timeLineData.getStatuses().size() != 0) {
            ActivitySharePrefHelper.putShareData(GlobalContext.getGlobalContext(), "max_id", String.valueOf(timeLineData.getStatuses().get(timeLineData.getStatuses().size() - 1).getId()));
        }
    }

    // 缓存过期，删除数据库中的数据
    public void removeData() {
        Extra extra = new Extra(App.getUserInfo().getUid(), Utils.generateMD5(key));
        SqliteUtility.getInstance("weiboDB").deleteAll(extra, FriendsTimeLine.StatusesBean.class);
    }
}
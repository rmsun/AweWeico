package com.nimrag.kevin.aweweico.cache;

import com.nimrag.kevin.aweweico.App;
import com.nimrag.kevin.aweweico.lib.ICache;
import com.nimrag.kevin.aweweico.lib.IResult;
import com.nimrag.kevin.aweweico.lib.Utils;
import com.nimrag.kevin.aweweico.lib.orm.SqliteUtility;
import com.nimrag.kevin.aweweico.lib.orm.extra.Extra;
import com.nimrag.kevin.aweweico.sinasdk.bean.FriendsTimeLine;

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
    public IResult getData() {
        return null;
    }

    @Override
    public void setData(IResult result) {
        /**
         * 缓存策略：刷新时返回微博信息的数量大于等于20条，先清空缓存，再写入。
         * 加载更多时，直接插库，数据库缓存的最大数目为60条
         */
        Extra extra = new Extra(App.getUserInfo().getUid(), Utils.generateMD5(key));
        SqliteUtility.getInstance("weiboDB").insert(extra, ((FriendsTimeLine)result).getStatuses());
    }
}
package com.nimrag.kevin.aweweico.lib;

import com.nimrag.kevin.aweweico.cache.FriendsTimeLineCache;
import com.nimrag.kevin.aweweico.sinasdk.bean.FriendsTimeLine;

/**
 * Created by kevin on 2017/2/28.
 */

public abstract class BizLogic implements IHttpUtility {

    @Override
    public <T> T doGet(HttpConfig config, String url, Params urlParams, Class<T> responseCls) {
        /**
         * 先向缓存请求数据，如果没有请求到，再向服务器请求
         */
        T result = null;
        // 当前用户所关注用户的最新微博信息
        if (url.toLowerCase().contains("friends_timeline")) {
            result = (T)(FriendsTimeLineCache.getInstance().getData());
            // cache中没有数据则向服务器请求
            if (result == null) {
                result = getHttpUtility().doGet(config, url, urlParams, responseCls);
                if (result != null) {
                    FriendsTimeLineCache.getInstance().setData((IResult)result);
                }
            }
        }
        return result;
    }

    @Override
    public <T> T doPost(HttpConfig config, String url, Params urlParams, Params bodyParams, Object requestObject, Class<T> responseCls) {
        return getHttpUtility().doPost(config, url, urlParams, bodyParams, requestObject, responseCls);
    }

    private IHttpUtility getHttpUtility() {
        return new DefaultHttpUtility();
    }
}

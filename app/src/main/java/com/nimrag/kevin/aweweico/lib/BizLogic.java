package com.nimrag.kevin.aweweico.lib;

/**
 * Created by kevin on 2017/2/28.
 */

public class BizLogic implements IHttpUtility {

    @Override
    public <T> T doGet(HttpConfig config, String url, Params urlParams, Class<T> responseCls) {
        return getHttpUtility().doGet(config, url, urlParams, responseCls);
    }

    @Override
    public <T> T doPost(HttpConfig config, String url, Params urlParams, Params bodyParams, Object requestObject, Class<T> responseCls) {
        return getHttpUtility().doPost(config, url, urlParams, bodyParams, requestObject, responseCls);
    }

    private IHttpUtility getHttpUtility() {
        return new DefaultHttpUtility();
    }
}

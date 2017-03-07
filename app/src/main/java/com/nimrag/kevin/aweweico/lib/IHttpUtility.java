package com.nimrag.kevin.aweweico.lib;

import java.util.Objects;

/**
 * Created by kevin on 2017/2/28.
 */

public interface IHttpUtility {
    <T> T doGet(HttpConfig config, String url, Params urlParams, Class<T> responseCls);
    <T> T doPost(HttpConfig config, String url, Params urlParams, Params bodyParams, Object requestObject, Class<T> responseCls);
}

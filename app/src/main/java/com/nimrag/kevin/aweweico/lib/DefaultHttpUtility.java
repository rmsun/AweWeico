package com.nimrag.kevin.aweweico.lib;

import com.google.gson.Gson;
import com.nimrag.kevin.aweweico.App;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by kevin on 2017/2/28.
 */

public class DefaultHttpUtility implements IHttpUtility {

    private OkHttpClient mHttpClient = App.getHttpClient();

    @Override
    public <T> T doGet(HttpConfig config, String url, Params urlParams, Class<T> responseCls) {
        Request.Builder builder = createRequestBuilder(config, url, urlParams);
        return executeRequest(builder.build(), responseCls);
    }

    @Override
    public <T> T doPost(HttpConfig config, String url, Params urlParams, Params bodyParams, Object requestObject, Class<T> responseCls) {
        RequestBody body = null;
        Request.Builder builder = createRequestBuilder(config, url, urlParams);
        if (bodyParams != null) {

        }
        if (requestObject != null) {

        }
        if (bodyParams == null && requestObject == null) {
            body = RequestBody.create(null, new byte[0]);
            builder.post(body);
        }
        return executeRequest(builder.build(), responseCls);
    }

    private <T> T executeRequest(Request request, Class<T> responseCls) {
        try {
            Response response = mHttpClient.newCall(request).execute();
            return parseResponse(response, responseCls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> T parseResponse(Response response, Class<T> responseCls) {
        Gson gson = new Gson();
        try {
            T responseObj = gson.fromJson(response.body().string(), responseCls);
            return responseObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Request.Builder createRequestBuilder(HttpConfig onfig, String url, Params urlParams) {
        String requestUrl = null;
        if (urlParams != null) {
            requestUrl = url + "?" + ParamsUtil.enCodeToUrlParams(urlParams);
        }
        Request.Builder builder = new Request.Builder();
        builder.url(requestUrl);
        return builder;
    }
}

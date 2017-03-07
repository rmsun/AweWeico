package com.nimrag.kevin.aweweico.lib;

import java.util.HashMap;

/**
 * Created by kevin on 2017/2/28.
 */

public class HttpConfig {
    // Cookie
    public String cookie;
    // 服务器地址
    public String baseUrl;
    public HashMap<String, String> headerMap = new HashMap<String, String>();

    public void addHeader(String k, String v) {
        headerMap.put(k, v);
    }
}

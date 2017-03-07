package com.nimrag.kevin.aweweico.lib;

import java.net.URLEncoder;

/**
 * Created by kevin on 2017/2/27.
 */

public class ParamsUtil {

    ParamsUtil() {}

    public static Params deCodeUrl(String url) {
        String source = url;
        if (url.contains("?")) {
            source = url.substring(url.indexOf("?") + 1, url.length());
        }

        Params paramKeyValue = new Params();
        String[] deCodeParams = source.split("&");
        for (String s : deCodeParams) {
            String[] keyValue = s.split("=");
            paramKeyValue.addParam(keyValue[0], keyValue[1]);
        }

        return paramKeyValue;
    }

    public static String enCodeToUrlParams(Params params) {
        StringBuffer enCodedBuffer = new StringBuffer();

        for (String key : params.getKeys()) {
            if (params.getParamValue(key) == null)
                continue;
            if (enCodedBuffer.length() != 0) {
                enCodedBuffer.append("&");
            }
            enCodedBuffer.append(key + "=");
            enCodedBuffer.append(params.getEncodeAble() ? enCodeParams(params.getParamValue(key)) : params.getParamValue(key));
        }

        return enCodedBuffer.toString();
    }

    private static String enCodeParams(String value) {
        if (value == null) {
            return "";
        }

        String enCodeStr = null;
        try {
            enCodeStr = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        char ch;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < enCodeStr.length(); i++) {
            ch = enCodeStr.charAt(i);
            if (ch == '+') {
                buffer.append("%20");
            } else if (ch == '*') {
                buffer.append("%2A");
            } else if (ch == '%' && (i + 1) < enCodeStr.length() && enCodeStr.charAt(i + 1) == '7' && enCodeStr.charAt(i + 2) == 'E') {
                buffer.append('~');
            } else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }
}

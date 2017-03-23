package com.nimrag.kevin.aweweico.lib.orm.utils;

import com.nimrag.kevin.aweweico.lib.ActivitySharePrefHelper;
import com.nimrag.kevin.aweweico.lib.GlobalContext;

/**
 * Created by kevin on 2017/3/18.
 * 缓存时间帮助类
 */

public class CacheTimeUtils {

    /**
     * 保存cache数据的时间戳
     */
    public static void saveTime(String key) {
        String time = String.valueOf(System.currentTimeMillis() / 1000);
        ActivitySharePrefHelper.putShareData(GlobalContext.getGlobalContext(), key, time);
    }

    public static long getSaveTime(String key) {
        return Long.parseLong(ActivitySharePrefHelper.getShareData(GlobalContext.getGlobalContext(), key));
    }

    public static boolean outOfDate(String key) {
        long saveTime = getSaveTime(key);

        // cache过期时间先设为15min
        long currentTime = System.currentTimeMillis() / 1000;
        boolean expired = Math.abs((System.currentTimeMillis() / 1000 - saveTime) * 1000) >= 15 * 60 * 1000;
        return expired;
    }
}

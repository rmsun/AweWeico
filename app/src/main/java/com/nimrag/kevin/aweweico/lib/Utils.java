package com.nimrag.kevin.aweweico.lib;

import android.content.Context;
import android.database.DatabaseUtils;
import android.text.format.DateUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kevin on 2017/3/14.
 */

public class Utils {

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String generateMD5(String key) {
        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(key.getBytes());
            byte[] bytes = e.digest();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(255 & bytes[i]);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间转换
     * 把时间转换成：刚刚 x分钟前 x月x日x时x分等的形式
     */
    public static String convertTime(String time) {
        try {
            StringBuffer sb = new StringBuffer();
            Calendar createAtCal = Calendar.getInstance();
            // millis格式的时间
            if (time.length() == 13) {
                createAtCal.setTimeInMillis(Long.parseLong(time));
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzzz yyyy", Locale.ENGLISH);
                createAtCal.setTime(sdf.parse(time));
            }
            // 当前时间
            Calendar currentCal = Calendar.getInstance();
            currentCal.setTimeInMillis(System.currentTimeMillis());

            // 发布时间与现在的差值
            long diffTime = (currentCal.getTimeInMillis() - createAtCal.getTimeInMillis()) / 1000;
            // 同一月
            if (currentCal.get(Calendar.MONTH) == createAtCal.get(Calendar.MONTH)) {
                if (currentCal.get(Calendar.DAY_OF_MONTH) == createAtCal.get(Calendar.DAY_OF_MONTH)) {
                    if (diffTime < 3600 && diffTime >= 60) {
                        sb.append((diffTime / 60) + "分钟之前");
                    } else if (diffTime < 60) {
                        sb.append("刚刚");
                    } else {
                        sb.append("今天 ").append(DateUtils.formatDateTime(GlobalContext.getGlobalContext(), createAtCal.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
                    }
                // 昨天
                } else if (currentCal.get(Calendar.DAY_OF_MONTH) - createAtCal.get(Calendar.DAY_OF_MONTH) == 1) {
                    sb.append("昨天 ").append(DateUtils.formatDateTime(GlobalContext.getGlobalContext(), createAtCal.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
                }
            }
            if (sb.length() == 0) {
                sb.append(DateUtils.formatDateTime(GlobalContext.getGlobalContext(), createAtCal.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME));
            }
            String timeStr = sb.toString();
            // 不是同一年，加上年份
            if (currentCal.get(Calendar.YEAR) != createAtCal.get(Calendar.YEAR)) {
                timeStr = createAtCal.get(Calendar.YEAR) + " " + timeStr;
            }

            return timeStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析html超链接中的文本
     */
    public static String parseHtmlATag(String href) {
        String linkText = null;
        Pattern pattern = Pattern.compile("(>.+<)");
        Matcher matcher = pattern.matcher(href);
        if (matcher.find()) {
            String temp = matcher.group(1);
            linkText = temp.substring(1, temp.length() - 2);
        }
        return linkText;
    }
}

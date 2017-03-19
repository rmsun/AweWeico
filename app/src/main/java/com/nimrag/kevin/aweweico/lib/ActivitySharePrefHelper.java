package com.nimrag.kevin.aweweico.lib;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kevin on 2017/3/18.
 * SharePreference读写帮助类
 */

public class ActivitySharePrefHelper {

    private static final String KEY = "com.nimrag.kevin.aweweico_key";

    private ActivitySharePrefHelper() {

    }

    public static String getShareData(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preference.getString(key, "");
    }

    public static String getShareData(Context context, String key, String defaultValue) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preference.getString(key, defaultValue);
    }

    public static int getIntShareData(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preference.getInt(key, 0);
    }

    public static int getIntShareData(Context context, String key, int defaultValue) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preference.getInt(key, defaultValue);
    }

    public static boolean getBooleanShareData(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preference.getBoolean(key, false);
    }

    public static boolean getBooleanShareData(Context context, String key, boolean defaultValue) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preference.getBoolean(key, defaultValue);
    }

    public static Set<String> getSetShareData(Context context, String key) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        return preference.getStringSet(key, new HashSet<String>());
    }

    public static void putShareData(Context context, String key, String value) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putIntShareData(Context context, String key, int value) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void putBooleanShareData(Context context, String key, boolean value) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void putSetShareData(Context context, String key, Set<String> value) {
        SharedPreferences preference = context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putStringSet(key, value);
        editor.commit();
    }
}

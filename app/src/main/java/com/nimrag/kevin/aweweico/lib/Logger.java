package com.nimrag.kevin.aweweico.lib;

import android.util.Log;

/**
 * Created by kevin on 2017/3/8.
 * Log的对外接口,依赖Logger2File
 */

public class Logger {

    public static final boolean DEBUG = true;
    private static final String TAG = "Logger";

    public static void e (String tag, String log) {
        if (DEBUG) {
            Log.e(TAG, log);
            Logger2File.log2File(tag, log);
        }
    }

    public static void d (String tag, String log) {
        if (DEBUG) {
            Log.d(TAG, log);
            Logger2File.log2File(tag, log);
        }
    }

    public static void v (String tag, String log) {
        if (DEBUG) {
            Log.v(TAG, log);
            Logger2File.log2File(tag, log);
        }
    }
}

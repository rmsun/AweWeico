package com.nimrag.kevin.aweweico.lib.orm;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by sunkevin on 2017/3/9.
 *
 * 数据库操作帮助类
 */

public class SqliteUtility {

    public static final String TAG = "SqliteUtility";

    /**
    * SqliteUtility缓存
    */
    private static Hashtable<String, SqliteUtility> dbCache = new Hashtable<String, SqliteUtility>();
    private String dbName;
    private SQLiteDatabase db;

    SqliteUtility(String dbName, SQLiteDatabase db) {
        this.dbName = dbName;
        this.db = db;

        dbCache.put(dbName, this);
    }

    public static SqliteUtility getInstance() { return getInstance(SqliteUtilityBuilder.DEFAULT_DB); }
    public static SqliteUtility getInstance(String dbName) { return dbCache.get(dbName); }

    /**
     * selectById方法
     */
}

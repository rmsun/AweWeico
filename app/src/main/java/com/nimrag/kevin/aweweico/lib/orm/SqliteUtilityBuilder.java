package com.nimrag.kevin.aweweico.lib.orm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by sunkevin on 2017/3/13.
 * SqliteUtility辅助类
 */

public class SqliteUtilityBuilder {

    public static final String TAG = SqliteUtility.TAG;
    public static final String DEFAULT_DB = "com_default_db";
    /**
     * db的sdcard路径
     */
    private String dbPathOfSdcard;
    private String dbName = DEFAULT_DB;
    /**
     * db版本
     */
    private int version = 1;
    private boolean sdcardDB = false;

    public SqliteUtilityBuilder configDbName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    public SqliteUtilityBuilder configVersion(int version) {
        this.version = version;
        return this;
    }

    public SqliteUtilityBuilder configSdcardPath(String path) {
        this.sdcardDB = true;
        this.dbPathOfSdcard = path;
        return this;
    }

    public SqliteUtility build(Context context) {
        SQLiteDatabase db = null;

        // 在sd卡上建库
        if (sdcardDB) {
            db = openSdcardDb(dbPathOfSdcard, dbName, version);
        } else {
            db = new SqliteDbHelper(context, dbName, version).getWritableDatabase();
        }

        return new SqliteUtility(dbName, db);
    }

    /**
     * 在sd卡上建数据库
     */
    private static SQLiteDatabase openSdcardDb(String path, String dbName, int version) {
        SQLiteDatabase db = null;
        File dbFile = new File(path + File.separator + dbName + ".db");

        if (dbFile.exists()) {
            db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
        } else {
            dbFile.getParentFile().mkdirs();
            try {
                if (dbFile.createNewFile()) {
                    db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 检查是否需要更新数据库
        if (db != null) {
            int currentDbVersion = db.getVersion();
            if (currentDbVersion < version) {
                // 先清库
                dropDb(db);

                db.beginTransaction();
                try {
                    db.setVersion(version);
                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
            return db;
        }
        throw new RuntimeException("open db fail, dbName = " + dbName + " path = " + path);
    }

    static class SqliteDbHelper extends SQLiteOpenHelper {
        SqliteDbHelper(Context context, String dbName, int version) {
            super(context, dbName, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            dropDb(db);
            onCreate(db);
        }
    }

    /**
     * 清库
     */
    private static void dropDb(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' AND name != 'sqlite_sequence'", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                db.execSQL("DROP TABLE " + cursor.getString(0));
            }
        }
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }
}

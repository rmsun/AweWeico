package com.nimrag.kevin.aweweico.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kevin on 2017/2/22.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "aweweico.db";

    private static final int VERSION = 1;

    public DBHelper(Context context) {super(context, DB_NAME, null, VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

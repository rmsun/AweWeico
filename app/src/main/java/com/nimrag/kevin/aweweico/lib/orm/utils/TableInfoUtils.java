package com.nimrag.kevin.aweweico.lib.orm.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nimrag.kevin.aweweico.lib.orm.SqliteUtility;
import com.nimrag.kevin.aweweico.lib.orm.annotation.TableName;
import com.nimrag.kevin.aweweico.lib.orm.extra.TableColumn;
import com.nimrag.kevin.aweweico.lib.orm.extra.TableInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kevin on 2017/3/12.
 * TableInfo工具类
 */

public class TableInfoUtils {

    public static final String TAG = SqliteUtility.TAG;

    /**
     * 缓存所有的TableInfo
     */
    private static final HashMap<String, TableInfo> tableInfoMap;
    static {
        tableInfoMap = new HashMap<String, TableInfo>();
    }

    public static <T> TableInfo exist(String dbName, Class<T> clazz) {
        return tableInfoMap.get(dbName + "-" + getTableName(clazz));
    }

    public static String getTableName(Class<?> clazz) {
        TableName tableName = clazz.getAnnotation(TableName.class);
        if (tableName == null || tableName.tableName().trim().length() == 0) {
            return clazz.getName().replace('.', '_');
        }
        return tableName.tableName();
    }

    public static <T> TableInfo newTable(String dbName, SQLiteDatabase db, Class<T> clazz) {
        Cursor cursor = null;
        TableInfo tableInfo = new TableInfo(clazz);
        tableInfoMap.put(dbName + '-' + getTableName(clazz), tableInfo);

        try {
            String sql = "SELECT COUNT(*) AS _count_ FROM sqlite_master WHERE type = 'table' AND name = '" + tableInfo.getTableName() + "' ";
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    cursor.close();

                    cursor = db.rawQuery("PRAGMA table_info" + "(" + tableInfo.getTableName() + ")", null);
                    // table所有字段名称
                    List<String> tableColumns = new ArrayList<String>();
                    if (cursor != null & cursor.moveToNext()) {
                        do {
                            tableColumns.add(cursor.getString(cursor.getColumnIndex("name")));
                        } while (cursor.moveToNext());
                    }
                    cursor.close();

                    // 对象的所有字段
                    List<String> propertyList = new ArrayList<String>();
                    for (TableColumn column : tableInfo.getColumns()) {
                        propertyList.add(column.getColumnName());
                    }

                    // 检查是否有新加字段
                    List<String> newFieldList = new ArrayList<String>();
                    for (String property : propertyList) {
                        if (tableInfo.getPrimaryKey().equals(property)) {
                            continue;
                        }
                        boolean isNew = true;
                        for (String columnName : tableColumns) {
                            if (columnName.equals(property)) {
                                isNew = false;
                                break;
                            }
                        }
                        if (isNew) {
                            newFieldList.add(property);
                        }
                    }

                    // 数据库表加新添加字段
                    for (String newField : newFieldList) {
                        db.execSQL(String.format("ALTER TABLE %s ADD %s TEXT", tableInfo.getTableName(), newField));
                    }

                    return tableInfo;
                 }
            }

            // 表在数据库中不存在，新建
            String createTableSql = SqlUtils.getTableSql(tableInfo);
            db.execSQL(createTableSql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

        return tableInfo;
    }
}

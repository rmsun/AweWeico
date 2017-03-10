package com.nimrag.kevin.aweweico.lib.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.nimrag.kevin.aweweico.lib.orm.annotation.AutoIncrementPrimaryKey;
import com.nimrag.kevin.aweweico.lib.orm.extra.Extra;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

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
     * @param extra 要查询的列
     * @param clazz 映射对象的class对象
     * @param id
     * @return 数据映射的对象
     */
    public <T> T selecById(Extra extra, Class<T> clazz, Object id) {
        try {
            TableInfo tableInfo = checkTableInfo(clazz);

            String selection = String.format(" %s = ? ", tableInfo.getPrimaryKey().getColumn());
            String extraSelection = SqlUtils.appendExtraWhereClause(extra);
            if (!TextUtils.isEmpty(extraSelection)) {
                selection = String.format("%s and %s", selection, extraSelection);
            }

            List<String> selectionArgList = new ArrayList<String>();
            selectionArgList.add(String.valueOf(id));
            String[] extraSelectionArgs = SqlUtils.appendExtraWhereArgs(extra);
            if (extraSelectionArgs != null && extraSelectionArgs.length > 0) {
                selectionArgList.addAll(Arrays.asList(extraSelectionArgs));
            }
            String[] selectionArags = selectionArgList.toArray(new String[0]);

            List<T> list = select(clazz, selection, selectionArags, null, null, null, null);
            if (list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  select方法
     *  @param extra 要查询的列
     *  @param clazz 映射对象的class对象
     *  @return 数据映射对象
     */
    public <T> List<T> select(Extra extra, Class<T> clazz) {
        String selection = SqlitUtils.appendExtraWhereClause(extra);
        String[] selectionArgs = SqliteUtils.appExtraWhereClauseArgs(extra);

        return select(clazz, selection, selectionArgs, null, null, null, null);
    }

    /**
     * select方法
     * @param clazz 映射对象的class对象
     * @param selection 返回的列
     * @param selectionArgs 为selection中的占位符提供value
      */
    public <T> List<T> select(Class<T> clazz, String selection, String[] selectionArgs) {
        return select(clazz, selection, selectionArgs, null, null, null, null);
    }

    public <T> List<T> select(Class<T> clazz, String selection, String[] selectionArgs,
                              String groupBy, String having, String orderBy, String limit) {
        TableInfo tableInfo = checkTable(clazz);

        ArrayList<T> list = new ArrayList<T>();
        List<String> columnList = new ArrayList<String>();
        columnList.add(tableInfo.getPrimaryKey().getColumn());
        for (TableColumn tableColumn : tableInfo.getColumn) {
            columnList.add(tableColumn.getColumn());
        }

        Cursor cursor = db.query(tableInfo.getTableName(), columnList.toArray(new String[0]), selection, selectionArgs, groupBy, having, orderBy, limit);
        try {
            if (cursor.moveToFirst()) {
                do {
                    try {
                        T entity = clazz.newInstance();
                        // 绑定主键
                        bindSelectValue(entity, cursor, tableInfo.getPrimaryKey());
                        // 绑定除主键外的其他数据
                        for (TableColumn column : tableInfo.getColumn()) {
                            bindSelectValue(entity, cursor, column);
                        }
                        list.add(entity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } while (cursor.moveToNext());
        } finally {
            cursor.close();
        }

        return list;
    }

    /**
     * insert方法
     * @param extra
     * @param entities 插入数据对应的对象
     */
    public <T> void insert(Extra extra, T... entities) {
        try {
            if (entities != null && entities.length > 0) {
                insert(extra, Arrays.asList(entities));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void insert(Extra extra, List<T> entityList) {
        try {
            insert(extra, entityList, "INSERT OR IGNORE INFO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void insertOrReplace(Extra extra, T... entities) {
        try {
            if (entities != null && entities.length > 0) {
                insert(extra, Arrays.asList(entities), "INSERT OR REPLACE INFO");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void insertOrReplace(Extra extra, List<T> entityList) {
        try {
            insert(extra, entityList, "INSERT OR REPLACE INFO");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * insert方法
     * @param extra
     * @param entityList 插入数据对应的对象
     * @param insertInfo 额外信息，用与判断存在主键实体时要ignore还是replace
     */
    public <T> void insert(Extra extra, List<T> entityList, String insertInfo) {
        if (entityList == null || entityList.size() == 0) {
            return;
        }

        TableInfo tableInfo = checkTable(entityList.get(0).getClass());
        synchronized (tableInfo) {
            db.beginTransaction();
            try {
                String sql = SqliteUtils.createSqlInsert(insertInfo, tableInfo);
                SQLiteStatement insertStatement = db.compileStatement(sql);
                for (T entity : entityList) {
                    bindInsertValue(extra, insertStatement, tableInfo, entity);
                    insertStatement.execute();
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

            if (entityList.size() == 1 && tableInfo.getPrimaryKey() instanceof AutoIncrementPrimaryKey) {
                Cursor cursor = null;
                try {
                    cursor = db.rawQuery("select last_insert_rowid() from " + tabInfo.getTableName(), null);
                    if (cursor.moveToFirst()) {
                        int newId = cursor.getInt(0);
                        T bean = entityList.get(0);
                        try {
                            tableInfo.getPrimaryKey().getField().setAccessible(true);
                            tableInfo.getPrimaryKey().setField().set(bean, newId);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
    }

    /**
     * update方法
     * @param extra
     * @param entities 要更新数据的对象
     */
    public <T> void update(Extra extra, T... entities) {
        innerUpdate(extra, Arrays.asList(entities));
    }

    public <T> void update(Extra extra, List<T> entityList) {
        innerUpdate(extra, entityList);
    }

    /**
     * innerUpdate方法,update的真正实现
     * @param extra
     * @param entityList
     */
    private <T> void innerUpdate(Extra extra, List<T> entityList) {
        try {
            if (entityList != null && entityList.size() > 0) {
                TableInfo tableInfo = checkTable(entityList.get(0).getClass());

                for (int i = 0; i < entityList.size(); i++) {
                    // 初始化update所需用的参数
                    T entity = entityList.get(i);
                    tableInfo.getPrimaryKey().getField().setAccessible(true);
                    Object id = tableInfo.getPrimaryKey().getField().get(entity);
                    String whereClause = String.format(" %s = ? ", tableInfo.getPrimaryKey().getColumn());
                    String extraSelection = SqlUtils.appendExtraWhereClause(extra);
                    if (!TextUtils.isEmpty(extraSelection)) {
                        whereClause = String.format("%s and %s", whereClause, extraSelection);
                    }
                    List<String> selectionArgList = new ArrayList<String>();
                    selectionArgList.add(String.valueOf(id));
                    String[] extraSelectionArgs = SqlUtils.appendExtraWhereArgs(extra);
                    if (extraSelectionArgs != null && extraSelectionArgs.length > 0) {
                        selectionArgList.addAll(Arrays.asList(extraSelectionArgs));
                    }
                    String[] whereArgs = selectionArgList.toArray(new String[0]);
                    ContentValues values = new ContentValues();
                    for (TableColumn column : tableInfo.getColumns()) {
                        bindValue(values, column, entity);
                    }

                    int rowId = db.update(tableInfo.getTableName(), values, whereClause, whereArgs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void update(Class<T> clazz, ContentValues values, String whereClause, String[] whereArgs) {
        try {
            TableInfo tableInfo = checkTable(clazz);
            return db.update(tableInfo.getTableName, values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * delete方法
     */
    public <T> void deleteAll(Extra extra, Class<T> clazz) {
        try {
            TableInfo tableInfo = checkTable(clazz);
            String whereClause = SqlUtils.appendExtraWhereClauseSql(extra);
            if (!TextUtils.isEmpty(whereClause)) {
                whereClause = " where " + whereClause;
            }
            String deleteSql = "DELETE FROM '" + tableInfo.getTableName() + "' " + whereClause;

            db.execSQL(deleteSql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void deleteById(Extra extra, Class<T> clazz, Object id) {
        try {
            TableInfo tableInfo = checkTable(clazz);
            String whereClause = String.format(" %s = ? ", tableInfo.getPrimaryKey().getColumn());
            String extraWhereClause = SqlUtils.appendExtraWhereClause(extra);
            if (!TextUtils.isEmpty(extraWhereClause)) {
                whereClause = String.format("%s and %s", whereClause, extraWhereClause);
            }
            List<String> whereArgsList = new ArrayList<String>();
            whereArgsList.add(String.valueOf(id));
            String[] extraWhereArgs = SqlUtils.appendExtraWhereArgs(extra);
            if (extraWhereArgs != null && extraWhereArgs.length > 0) {
                whereArgsList.addAll(Arrays.asList(extraWhereArgs));
            }
            String[] whereArags = whereArgsList.toArray(new String[0]);

            int rowCount = db.delete(tableInfo.getTableName(), whereClause, whereArags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void delete(Class<T> clazz, String whereClause, String[] whereArgs) {
        try {
            TableInfo tableInfo = checkTable(clazz);
            db.delete(tableInfo.getTableName(), whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* *
     * 统计方法
     */
    public <T> long sum(Class<T> clazz, String column, String whereClause, String[] whereArgs) {
        TableInfo tableInfo = checkTable(clazz);
        if (TextUtils.isEmpty(column)) {
            return 0;
        }
        String sumSql = null;
        if (TextUtils.isEmpty(whereClause)) {
            sumSql = String.format(" select sum(%s) as _sum_ from %s ", column, tableInfo.getTableName());
        } else {
            sumSql = String.format(" select sum(%s) as _sum_ from %s where %s ", column, tableInfo.getTableName(), whereClause);
        }

        try {
            Cursor cursor = db.rawQuery(sumSql, whereArgs);
            if (cursor.moveToFirst()) {
                long sum = cursor.getLong(cursor.getColumnIndex("_sum_"));
                cursor.close();
                return sum;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public <T> long count(Class<T> clazz, String column, String whereClause, String[] whereArags) {
        if (column == null) {
            return 0;
        }

        TableInfo tableInfo = checkTable(clazz);
        String countSql;
        if (TextUtils.isEmpty(whereClause)) {
            countSql = String.format(" select count(%s) as _count_ from %s ", column, tableInfo.getTableName());
        } else {
            countSql = String.format(" select count(%s) as _count_ from %s where %s ", column, tableInfo.getTableName(), whereClause);
        }

        try {
            Cursor cursor = db.rawQuery(countSql, whereArags);
            if (cursor.moveToFirst()) {
                long count = cursor.getLong(cursor.getColumnIndex("_count_"));
                cursor.close();
                return count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * bindSelectValue方法,根据数据库返回的value，初始化映射对象
     * @param entity 映射对象
     * @param cursor 数据库cursor
     * @param TableColumn 列信息
     */
    public <T> void bindSelecValue(T entity, Cursor cursor, TableColumn column) {
        Field field = column.getField();
        field.setAccessible(true);

        try {
            if (field.getType().getName().equals("int") ||
                    field.getType().getName().equals("java.lang.Integer")) {
                field.set(entity, cursor.getInt(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("long") ||
                    field.getType().getName().equals("java.lang.Long")) {
                field.set(entity, cursor.getLong(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("float") ||
                    field.getType().getName().equals("java.lang.Float")) {
                field.set(entity, cursor.getFloat(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("double") ||
                    field.getType().getName().equals("java.lang.Double")) {
                field.set(entity, cursor.getDouble(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("boolean") ||
                    field.getType().getName().equals("java.lang.Boolean")) {
                field.set(entity, cursor.getString(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("char") ||
                    field.getType().getName().equals("java.lang.Character")) {
                field.set(entity, cursor.getString(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("byte") ||
                    field.getType().getName().equals("java.lang.Byte")) {
                field.set(entity, cursor.getInt(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("short") ||
                    field.getType().getName().equals("java.lang.Short")) {
                field.set(entity, cursor.getShort(column.getColumn()));
            } else if (field.getType().getName().equals("java.lang.String")) {
                field.set(entity, cursor.getString(cursor.getColumnIndex(column.getColumn())));
            } else if (field.getType().getName().equals("[B")) {
                field.set(entity, cursor.getBlob(cursor.getColumnIndex(column.getColumn())));
            } else {
                Gson gson = new Gson();
                String text = cursor.getString(cursor.getColumnIndex(column.getColumin()));
                try {
                    field.set(entity, gson.fromJson(text, field.getGenericType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 检查表是否则存在,不存取在则建表
     * @param clazz 表映射对象
     */
    public <T> TableInfo checkTable(Class<T> clazz) {
        TableInfo tableInfo = TableInfoUtils.exist(dbName, clazz);
        if (tableInfo != null) {
            ;
        } else {
            tableInfo = TableInfoUtils.newTable(dbName, db, clazz);
        }
        return tableInfo;
    }
}

package com.nimrag.kevin.aweweico.lib.orm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import com.nimrag.kevin.aweweico.lib.orm.annotation.AutoIncrementPrimaryKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        String selectionArgs = SqliteUtils.appExtraWhereClauseArgs(extra);

        return select(clazz, selection, selectionArgs, null, null, null, null);
    }

    /**
     * select方法
     * @param clazz 映射对象的class对象
     * @param selection 返回的列
     * @param selectionArgs 为selection中的占位符提供value
      */
    public <T> List<T> select(Class<T> clazz, String selection, String[] selectionArgs) {
        return select(clazz, selection, selectionArgs, null, null, null);
    }

    public <T> List<T> select(Class<T>, String selection, String[] selectionArgs,
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
     *insert方法
     * @param extra
     * @param entityList 插入数据对应的对象
     * @param insertInfo 额外信息，用与判断存在主键实体时要ignore还是replace
     */
    public <T> insert(Extra extra, List<T> entityList, String insertInfo) {
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
    */
}

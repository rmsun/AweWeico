package com.nimrag.kevin.aweweico.lib.orm.utils;

import android.text.TextUtils;

import com.nimrag.kevin.aweweico.lib.orm.annotation.AutoIncrementPrimaryKey;
import com.nimrag.kevin.aweweico.lib.orm.extra.Extra;
import com.nimrag.kevin.aweweico.lib.orm.extra.TableColumn;
import com.nimrag.kevin.aweweico.lib.orm.extra.TableInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/3/12.
 */

public class SqlUtils {
    /**
     * 建表sql语句
     */
    public static String getTableSql(TableInfo tableInfo) {
        TableColumn primaryKey = tableInfo.getPrimaryKey();
        StringBuffer strSql = new StringBuffer();
        strSql.append("CREATE TABLE IF NOT EXISTS ");
        strSql.append(tableInfo.getTableName());
        strSql.append(" ( ");

        // 自增主键
        if (primaryKey instanceof AutoIncrementPrimaryKey) {
            strSql.append(" ")
                    .append(primaryKey.getColumnName())
                    .append(" ")
                    .append(" INTEGER PRIMARY KEY AUTOINCREMENT ,");
        } else {
            strSql.append(" ")
                    .append(primaryKey.getColumnName())
                    .append(" ")
                    .append(primaryKey.getColumnType())
                    .append(" NOT NULL ,");
        }

        for (TableColumn column : tableInfo.getColumns()) {
            strSql.append(" ")
                    .append(column.getColumnName())
                    .append(" ")
                    .append(" ")
                    .append(column.getColumnType())
                    .append(" ,");
        }

        strSql.append(" ").append(FieldUtils.OWNER).append(" text NOT NULL, ");
        strSql.append(" ").append(FieldUtils.KEY).append(" text NOT NULL, ");
        strSql.append(" ").append(FieldUtils.CREATEAT).append(" INTEGER NOT NULL");

        if (primaryKey instanceof AutoIncrementPrimaryKey) {

        } else {
            // 复合主键
            strSql.append(", PRIMARY KEY ( ").append(primaryKey.getColumnName()).append(" , ")
                    .append(FieldUtils.KEY).append(" , ")
                    .append(FieldUtils.OWNER).append(" )");
        }
        strSql.append(" )");
        return strSql.toString();
    }

    public static String createSqlInsert(String insertInto, TableInfo tableInfo) {
        List<TableColumn> columns = new ArrayList<TableColumn>();
        // 自增主键，不需要设置
        if (tableInfo.getPrimaryKey() instanceof AutoIncrementPrimaryKey) {

        } else {
            columns.add(tableInfo.getPrimaryKey());
        }
        columns.addAll(tableInfo.getColumns());
        columns.add(FieldUtils.getOwnerColumn());
        columns.add(FieldUtils.getKeyColumn());
        columns.add(FieldUtils.getCreateAtColumn());

        StringBuilder builder = new StringBuilder(insertInto);
        builder.append(tableInfo.getTableName()).append(" (");
        appendColumns(builder, columns);
        builder.append(") VALUES (");
        appendPlaceholders(builder, columns.size());
        builder.append(')');
        return builder.toString();
    }

    public static StringBuilder appendColumns(StringBuilder builder, List<TableColumn> columns) {
        int size = columns.size();
        for (int i = 0; i < size; i++) {
            builder.append('\'').append(columns.get(i).getColumnName()).append('\'');
            if (i < size - 1) {
                builder.append(',');
            }
        }
        return builder;
    }

    public static StringBuilder appendPlaceholders(StringBuilder builder, int count) {
        for (int i = 0; i < count; i ++) {
            if (i < count - 1) {
                builder.append("?,");
            } else {
                builder.append('?');
            }
        }
        return builder;
    }

    public static String appendExtraWhereClauseSql(Extra extra) {
        StringBuffer sb = new StringBuffer();
        if (extra == null || (TextUtils.isEmpty(extra.getKey())) && TextUtils.isEmpty(extra.getOwner())) {
            sb.append("");
        } else if (!TextUtils.isEmpty(extra.getKey()) && !TextUtils.isEmpty(extra.getOwner())) {
            sb.append(" ").append(FieldUtils.OWNER).append(" = '").append(extra.getOwner()).append("' ")
                    .append(" and ")
                    .append(FieldUtils.KEY).append(" ='").append(extra.getKey()).append("' ");
        } else if (!TextUtils.isEmpty(extra.getOwner())) {
            sb.append(" ").append(FieldUtils.OWNER).append(" = '").append(extra.getOwner()).append("' ");
        } else if (!TextUtils.isEmpty(extra.getKey())) {
            sb.append(" ").append(FieldUtils.KEY).append(" ='").append(extra.getKey()).append("' ");
        }

        return sb.toString();
    }

    public static String appendExtraWhereClause(Extra extra) {
        StringBuffer sb = new StringBuffer();
        if (extra == null || (TextUtils.isEmpty(extra.getKey())) && TextUtils.isEmpty(extra.getOwner())) {
            sb.append("");
        } else if (!TextUtils.isEmpty(extra.getKey()) && !TextUtils.isEmpty(extra.getOwner())) {
            sb.append(" ").append(FieldUtils.OWNER).append(" =? ")
                    .append(" and ")
                    .append(FieldUtils.KEY).append(" =?");
        } else if (!TextUtils.isEmpty(extra.getOwner())) {
            sb.append(" ").append(FieldUtils.OWNER).append(" =? ");
        } else if (!TextUtils.isEmpty(extra.getKey())) {
            sb.append(" ").append(FieldUtils.KEY).append(" =? ");
        }

        return sb.toString();
    }

    public static String[] appendExtraWhereArgs(Extra extra) {
        List<String> argList = new ArrayList<String>();
        if (extra != null) {
            if (!TextUtils.isEmpty(extra.getOwner())) {
                argList.add(extra.getKey());
            }
            if (!TextUtils.isEmpty(extra.getKey())) {
                argList.add(extra.getKey());
            }
        }

        return argList.toArray(new String[0]);
    }
}

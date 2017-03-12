package com.nimrag.kevin.aweweico.lib.orm.utils;

import android.app.ActionBar;

import com.nimrag.kevin.aweweico.lib.orm.extra.TableColumn;

/**
 * Created by kevin on 2017/3/12.
 */

public class FieldUtils {
    public static final String OWNER = "com_data_owner";
    public static final String KEY = "com_data_key";
    public static final String CREATEAT = "com_data_createat";

    public static TableColumn getOwnerColumn() {
        TableColumn column = new TableColumn();
        column.setColumnName(OWNER);
        return column;
    }

    public static TableColumn getKeyColumn() {
        TableColumn column = new TableColumn();
        column.setColumnName(KEY);
        return column;
    }

    public static TableColumn getCreateAtColumn() {
        TableColumn column = new TableColumn();
        column.setColumnName(CREATEAT);
        return column;
    }
}

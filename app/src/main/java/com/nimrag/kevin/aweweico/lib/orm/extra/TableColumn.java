package com.nimrag.kevin.aweweico.lib.orm.extra;

import java.lang.reflect.Field;

/**
 * Created by sunkevin on 2017/3/10.
 * 列
 */

public class TableColumn {

    /**
     * 数据类型
     */
    private String dataType;
    /**
     * 映射对象对应的field
     */
    private Field field;
    /**
     * 列名
     */
    private String columnName;
    /**
     * 列中对应数据的类型
     */
    private String columnType;

    public String getDataType() { return dataType; }
    public Field getField() { return field; }
    public String getColumnName() { return columnName; }
    public String getColumnType() { return columnType; }
    public void setDataType(String dataType) { this.dataType = dataType; }
    public void setField(Field field) { this.field = field; }
    public void setColumnName(String columnName) { this.columnName = columnName; }
    public void setColumnType(String columnType) { this.columnType = columnType; }
}

package com.nimrag.kevin.aweweico.lib.orm.extra;

import com.nimrag.kevin.aweweico.lib.orm.annotation.AutoIncrementPrimaryKey;
import com.nimrag.kevin.aweweico.lib.orm.annotation.PrimaryKey;
import com.nimrag.kevin.aweweico.lib.orm.utils.TableInfoUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/3/10.
 * 表信息
 */

public class TableInfo {

    /**
     * TableInfo对应的class对象
     */
    private Class<?> clazz;
    /**
     * 主键
     */
    private TableColumn primaryKey;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表中字段
     */
    private List<TableColumn> columns;

    public TableInfo(Class<?> clazz) {
        this.clazz = clazz;
        initTableInfo();
    }

    private void initTableInfo() {
        columns = new ArrayList<TableColumn>();
        setTableName();
        //初始化所有字段
        setColumns(clazz);
        // 主键不能为空
        if (primaryKey == null) {
            throw new RuntimeException("类 " + clazz.getSimpleName() + " 没有设置主键");
        }
    }

    private void setTableName() { tableName = TableInfoUtils.getTableName(clazz); }

    private void setColumns(Class<?> clazz) {
        if (clazz == null || "Object".equalsIgnoreCase(clazz.getSimpleName())) {
            return;
        }

        Field fields[] = clazz.getDeclaredFields();
        for (Field field : fields) {
            // 设置主键
            if (primaryKey == null){
                PrimaryKey annotationField = field.getAnnotation(PrimaryKey.class);
                if (annotationField != null) {
                    primaryKey = new TableColumn();
                    primaryKey.setColumnName(annotationField.column());
                    initColumn(field, primaryKey);
                    continue;
                } else {
                    AutoIncrementPrimaryKey autoIncrementPrimaryKey = field.getAnnotation(AutoIncrementPrimaryKey.class);
                    if (autoIncrementPrimaryKey != null) {
                        primaryKey = new TableColumn();
                        primaryKey.setColumnName(annotationField.column());
                        initColumn(field, primaryKey);
                        continue;
                    }
                }
            }

            // 不包括序列化字段
            if ("serialVersionUID".equals(field.getName())) {
                continue;
            }
            // 不包括Gradle编译产生的字段
            if ("$change".equals(field.getName())) {
                continue;
            }

            TableColumn column = new TableColumn();
            column.setColumnName(field.getName());
            initColumn(field, column);
            // 添加到字段集合
            columns.add(column);
        }
        // 父类的属性来映射Table的字段
        setColumns(clazz.getSuperclass());
    }

    private void initColumn(Field field, TableColumn column) {
        column.setField(field);
        if (field.getType().getName().equals("int") ||
                field.getType().getName().equals("java.lang.Integer")) {
            column.setDataType("int");
            column.setColumnType("INTEGER");
        } else if (field.getType().getName().equals("long") ||
                field.getType().getName().equals("java.lang.Long")) {
            column.setDataType("long");
            column.setColumnType("INTEGER");
        } else if (field.getType().getName().equals("float") ||
                field.getType().getName().equals("java.lang.Float")) {
            column.setDataType("float");
            column.setColumnType("REAL");
        } else if (field.getType().getName().equals("double") ||
                field.getType().getName().equals("java.lang.Double")) {
            column.setDataType("double");
            column.setColumnType("REAL");
        } else if (field.getType().getName().equals("boolean") ||
                field.getType().getName().equals("java.lang.Boolean")) {
            column.setDataType("boolean");
            column.setColumnType("TEXT");
        } else if (field.getType().getName().equals("char") ||
                field.getType().getName().equals("java.lang.Character")) {
            column.setDataType("char");
            column.setColumnType("TEXT");
        } else if (field.getType().getName().equals("byte") ||
                field.getType().getName().equals("java.lang.Byte")) {
            column.setDataType("byte");
            column.setColumnType("INTEGER");
        } else if (field.getType().getName().equals("short") ||
                field.getType().getName().equals("java.lang.Short")) {
            column.setDataType("short");
            column.setColumnType("TEXT");
        } else if (field.getType().getName().equals("java.lang.String")) {
            column.setDataType("string");
            column.setColumnType("TEXT");
        } else if (field.getType().getName().equals("[B")) {
            column.setDataType("blob");
            column.setColumnType("BLOB");
        } else {
            column.setDataType("object");
            column.setColumnType("TEXT");
        }
    }

    public Class<?> getClazz() { return clazz; }

    public TableColumn getPrimaryKey() { return primaryKey; }

    public String getTableName() { return tableName; }

    public List<TableColumn> getColumns() { return columns; }
}

package com.oa.core.bean.util;

import java.io.Serializable;

public class Table implements Serializable {
    private String tableName;
    private String pk;
    private String prefix;
    private String field;
    private String value;
    private String where;

    public Table(String tableName, String pk) {
        super();
        this.tableName = tableName;
        this.pk = pk;
    }

    public Table(String tableName, String pk, String prefix) {
        super();
        this.tableName = tableName;
        this.pk = pk;
        this.prefix = prefix;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }
}

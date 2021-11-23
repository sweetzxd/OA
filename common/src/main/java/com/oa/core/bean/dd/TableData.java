package com.oa.core.bean.dd;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;

public class TableData  implements Serializable {
    private static final long serialVersionUID = 1L;

    //数据表名 主键
    private String tableName;

    //数据表名称 汉字名
    private String tableTitle;

    //数据表字段 字段ID
    private String tableField;

    private int curStatus;
    private String recName;
    private Timestamp recTime;
    private String modName;
    private Timestamp modTime;
    private String delName;
    private Timestamp delTime;

    private int startRow;
    private int endRow;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableTitle() {
        return tableTitle;
    }

    public void setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
    }

    public String getTableField() { return tableField; }

    public void setTableField(String tableField) { this.tableField = tableField; }

    public int getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(int curStatus) {
        this.curStatus = curStatus;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public Timestamp getRecTime() {
        return recTime;
    }

    public void setRecTime(Timestamp recTime) {
        this.recTime = recTime;
    }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public Timestamp getModTime() {
        return modTime;
    }

    public void setModTime(Timestamp modTime) {
        this.modTime = modTime;
    }

    public String getDelName() {
        return delName;
    }

    public void setDelName(String delName) {
        this.delName = delName;
    }

    public Timestamp getDelTime() {
        return delTime;
    }

    public void setDelTime(Timestamp delTime) {
        this.delTime = delTime;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public Hashtable<String,String> getSqlField(){
        Hashtable<String,String> ht = new Hashtable<>();

        String field = "tableName,tableTitle,tableField" +
                ",curStatus,recName,recTime,modName,modTime,delName,delTime";
        ht.put("field",field);

        String tableName = getStringValue(getTableName());
        String tableTitle = getStringValue(getTableTitle());
        String tableField = getStringValue(getTableField());

        int curStatus = 2;
        String recName = getStringValue(getRecName());
        String recTime = getStringValue(getRecTime());
        String modName = getStringValue(getModName());
        String modTime = getStringValue(getModTime());
        String delName = getStringValue(getDelName());
        String delTime = getStringValue(getDelTime());

        String value = tableName+","+tableTitle+","+tableField+"" +
                ","+curStatus+","+recName+","+recTime+","+modName+","+modTime+","+delName+","+delTime;
        ht.put("value",value);
        return ht;
    }

    public String getStringValue(Object val){
        if(val!=null){
            return "'"+val+"'";
        }else{
            return null;
        }
    }
}

package com.oa.core.bean.dd;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Hashtable;

public class TaskData  implements Serializable {
    private static final long serialVersionUID = 1L;

    //任务主键
    private String taskName;

    //任务名称 汉字名
    private String taskTitle;

    //任务类型 列表、表单
    private String taskType;

    //数据表 关联键（隐藏）
    private String tableName;

    //数据表字段 字段ID 有排序
    private String taskField;

    private int curStatus;
    private String recName;
    private Timestamp recTime;
    private String modName;
    private Timestamp modTime;
    private String delName;
    private Timestamp delTime;

    private int startRow;
    private int endRow;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTaskField() {
        return taskField;
    }

    public void setTaskField(String taskField) {
        this.taskField = taskField;
    }

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

    public Timestamp getRecTime() { return recTime; }

    public void setRecTime(Timestamp recTime) { this.recTime = recTime; }

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public Timestamp getModTime() { return modTime; }

    public void setModTime(Timestamp modTime) { this.modTime = modTime; }

    public String getDelName() {
        return delName;
    }

    public void setDelName(String delName) {
        this.delName = delName;
    }

    public Timestamp getDelTime() { return delTime; }

    public void setDelTime(Timestamp delTime) { this.delTime = delTime; }

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

        String field = "taskName,taskTitle,taskType,tableName,taskField" +
                ",curStatus,recName,recTime,modName,modTime,delName,delTime";
        ht.put("field",field);

        String taskName = getStringValue(getTaskName());
        String taskTitle = getStringValue(getTaskTitle());
        String taskType = getStringValue(getTaskType());
        String tableName = getStringValue(getTableName());
        String taskField = getStringValue(getTaskField());
        int curStatus = 2;
        String recName = getStringValue(getRecName());
        String recTime = getStringValue(getRecTime());
        String modName = getStringValue(getModName());
        String modTime = getStringValue(getModTime());
        String delName = getStringValue(getDelName());
        String delTime = getStringValue(getDelTime());

        String value = taskName+","+taskTitle+","+taskType+","+tableName+","+taskField+"" +
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
